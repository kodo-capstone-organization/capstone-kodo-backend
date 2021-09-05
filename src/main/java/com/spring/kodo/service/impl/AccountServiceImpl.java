package com.spring.kodo.service.impl;

import com.spring.kodo.entity.*;
import com.spring.kodo.repository.AccountRepository;
import com.spring.kodo.repository.CourseRepository;
import com.spring.kodo.repository.ForumPostRepository;
import com.spring.kodo.repository.StudentAttemptRepository;
import com.spring.kodo.service.inter.*;
import com.spring.kodo.util.MessageFormatterUtil;
import com.spring.kodo.util.cryptography.CryptographicHelper;
import com.spring.kodo.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class AccountServiceImpl implements AccountService
{
    @Autowired // With this annotation, we do not to populate AccountRepository in this class' constructor
    private AccountRepository accountRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentAttemptRepository studentAttemptRepository;
    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private TagService tagService;

    @Autowired
    private EnrolledCourseService enrolledCourseService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ForumThreadService forumThreadService;

    @Autowired
    private ForumPostService forumPostService;

    @Autowired
    private StudentAttemptService studentAttemptService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public AccountServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Account createNewAccount(Account newAccount, List<String> tagTitles) throws TagNameExistsException, InputDataValidationException, UnknownPersistenceException, AccountUsernameOrEmailExistsException
    {
        try
        {
            Set<ConstraintViolation<Account>> constraintViolations = validator.validate(newAccount);
            if (constraintViolations.isEmpty())
            {
                // Persist Account
                accountRepository.saveAndFlush(newAccount);

                // Process Tags
                if (tagTitles != null && (!tagTitles.isEmpty()))
                {
                    for (String tagTitle : tagTitles)
                    {
                        Tag tag = tagService.getTagByTitleOrCreateNew(tagTitle);
                        try
                        {
                            newAccount = addTagToAccount(newAccount, tag);
                        }
                        catch (TagNotFoundException | AccountNotFoundException | UpdateAccountException ex)
                        {
                            // Since we are still in a creation step, these generic exceptions will probably not happen / can kinda be ignored
                            // e.g. acc not found, duplicate tag exception
                            continue;
                        }
                    }
                }

                return newAccount;
            }
            else
            {
                throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        catch (DataAccessException ex)
        {
            if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
            {
                throw new AccountUsernameOrEmailExistsException("Account username and/or email is already in use!");
            }
            else
            {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public Account getAccountByAccountId(Long accountId) throws AccountNotFoundException
    {
        Account account = accountRepository.findById(accountId).orElse(null);

        if (account != null)
        {
            return account;
        }
        else
        {
            throw new AccountNotFoundException("Account with ID: " + accountId + " does not exist!");
        }
    }

    @Override
    public Account getAccountByUsername(String username) throws AccountNotFoundException
    {
        Account account = accountRepository.findByUsername(username).orElse(null);

        if (account != null)
        {
            return account;
        }
        else
        {
            throw new AccountNotFoundException("Account with Username: " + username + " does not exist!");
        }
    }

    @Override
    public Account getAccountByEmail(String email) throws AccountNotFoundException
    {
        Account account = accountRepository.findByEmail(email).orElse(null);

        if (account != null)
        {
            return account;
        }
        else
        {
            throw new AccountNotFoundException("Account with Email: " + email + " does not exist!");
        }
    }

    @Override
    public Account getAccountByCourseId(Long courseId) throws AccountNotFoundException
    {
        Account account = accountRepository.findByCourseId(courseId).orElse(null);

        if (account != null)
        {
            return account;
        }
        else
        {
            throw new AccountNotFoundException("Account with Course ID: " + courseId + " does not exist!");
        }
    }

    @Override
    public Account getAccountByEnrolledCourseId(Long enrolledCourseId) throws AccountNotFoundException
    {
        Account account = accountRepository.findByEnrolledCourseId(enrolledCourseId).orElse(null);

        if (account != null)
        {
            return account;
        }
        else
        {
            throw new AccountNotFoundException("Account with EnrolledCourse ID: " + enrolledCourseId + " does not exist!");
        }
    }

    @Override
    public Account getAccountByStudentAttemptId(Long studentAttemptId) throws AccountNotFoundException
    {
        Account account = accountRepository.findByStudentAttemptId(studentAttemptId).orElse(null);

        if (account != null)
        {
            return account;
        }
        else
        {
            throw new AccountNotFoundException("Account with StudentAttempt ID: " + studentAttemptId + " does not exist!");
        }
    }

    @Override
    public List<Account> getAllAccounts()
    {
        List<Account> accounts = accountRepository.findAll();
        return accounts;
    }

    @Override
    public Account updateAccount(
            Account account,
            List<String> tagTitles,
            List<Long> enrolledCourseIds,
            List<Long> courseIds,
            List<Long> forumThreadIds,
            List<Long> forumPostIds,
            List<Long> studentAttemptIds
    )
            throws AccountNotFoundException, TagNotFoundException,
            UpdateAccountException, EnrolledCourseNotFoundException,
            CourseNotFoundException, StudentAttemptNotFoundException,
            ForumThreadNotFoundException, ForumPostNotFoundException,
            TagNameExistsException, UnknownPersistenceException,
            InputDataValidationException
    {

        if (account != null && account.getAccountId() != null)
        {
            Account accountToUpdate = null;
            Set<ConstraintViolation<Account>> constraintViolations = validator.validate(account);

            if (constraintViolations.isEmpty())
            {
                // Get managed instance of account to be updated
                accountToUpdate = getAccountByAccountId(account.getAccountId());

                if (accountToUpdate.getUsername().equals(account.getUsername()))
                {
                    // Update tags (interests) - Unidirectional
                    if (tagTitles != null)
                    {
                        accountToUpdate.getInterests().clear();
                        for (String tagTitle : tagTitles)
                        {
                            Tag tag = tagService.getTagByTitleOrCreateNew(tagTitle);
                            addTagToAccount(accountToUpdate, tag);
                        }
                    }

                    // Update enrolled courses - Unidirectional
                    if (enrolledCourseIds != null)
                    {
                        accountToUpdate.getEnrolledCourses().clear();
                        for (Long enrolledCourseId : enrolledCourseIds)
                        {
                            EnrolledCourse enrolledCourse = enrolledCourseService.getEnrolledCourseByEnrolledCourseId(enrolledCourseId);
                            addEnrolledCourseToAccount(accountToUpdate, enrolledCourse);
                        }
                    }

                    // Update courses (as a tutor) - Bidirectional, 1-to-many
//                    if (courseIds != null)
//                    {
//                        for (Course course : accountToUpdate.getCourses())
//                        {
//                            course.setTutor(null);
//                        }
//
//                        accountToUpdate.getCourses().clear();
//                        for (Long courseId : courseIds)
//                        {
//                            Course course = courseService.getCourseByCourseId(courseId);
//                            addCourseToAccount(accountToUpdate, course);
//                        }
//                    }

                    // Update studentAttempts - Unidirectional
                    if (studentAttemptIds != null)
                    {
                        accountToUpdate.getStudentAttempts().clear();
                        for (Long studentAttemptId : studentAttemptIds)
                        {
                            StudentAttempt studentAttempt = studentAttemptService.getStudentAttemptByStudentAttemptId(studentAttemptId);
                            addStudentAttemptToAccount(accountToUpdate, studentAttempt);
                        }
                    }

                    // Update other non-relational fields
                    accountToUpdate.setUsername(account.getUsername());
                    accountToUpdate.setName(account.getName());
                    accountToUpdate.setPassword(account.getPassword());
                    accountToUpdate.setSalt(account.getSalt());
                    accountToUpdate.setBio(account.getBio());
                    accountToUpdate.setEmail(account.getEmail());
                    accountToUpdate.setDisplayPictureUrl(account.getDisplayPictureUrl());
                    accountToUpdate.setIsAdmin(account.getIsAdmin());
                    accountToUpdate.setIsActive(account.getIsActive());
                    accountToUpdate.setStripeAccountId(account.getStripeAccountId());

                    return accountRepository.saveAndFlush(accountToUpdate);
                }
                else
                {
                    throw new UpdateAccountException("Username of account record to be updated does not match the existing record");
                }
            }
            else
            {
                throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new AccountNotFoundException("Account ID not provided for account to be updated");
        }
    }

    @Override
    public Account addTagToAccount(Account account, Tag tag) throws AccountNotFoundException, TagNotFoundException, UpdateAccountException
    {
        account = getAccountByAccountId(account.getAccountId());
        tag = tagService.getTagByTagId(tag.getTagId());

        if (!account.getInterests().contains(tag))
        {
            account.getInterests().add(tag);
        }
        else
        {
            throw new UpdateAccountException("Unable to add tag with title: " + tag.getTitle() +
                    " to account with ID: " + account.getAccountId() + " as tag is already linked to this account");
        }

        accountRepository.saveAndFlush(account);
        return account;
    }

    @Override
    public Account addEnrolledCourseToAccount(Account account, EnrolledCourse enrolledCourse) throws UpdateAccountException, AccountNotFoundException, EnrolledCourseNotFoundException
    {
        if (account != null)
        {
            if (account.getAccountId() != null)
            {
                account = getAccountByAccountId(account.getAccountId());
                if (enrolledCourse != null)
                {
                    if (enrolledCourse.getEnrolledCourseId() != null)
                    {
                        enrolledCourse = enrolledCourseService.getEnrolledCourseByEnrolledCourseId(enrolledCourse.getEnrolledCourseId());

                        if (!account.getEnrolledCourses().contains(enrolledCourse))
                        {
                            account.getEnrolledCourses().add(enrolledCourse);

                            accountRepository.save(account);
                            return account;
                        }
                        else
                        {
                            throw new UpdateAccountException("Account with ID " + account.getAccountId() + " already contains EnrolledCourse with ID " + enrolledCourse.getEnrolledCourseId());
                        }
                    }
                    else
                    {
                        throw new UpdateAccountException("EnrolledCourse ID cannot be null");
                    }
                }
                else
                {
                    throw new UpdateAccountException("EnrolledCourse cannot be null");
                }
            }
            else
            {
                throw new UpdateAccountException("Account ID cannot be null");
            }
        }
        else
        {
            throw new UpdateAccountException("Account cannot be null");
        }
    }

    @Override
    public Account addCourseToAccount(Account account, Course course) throws UpdateAccountException, AccountNotFoundException, CourseNotFoundException
    {
        if (account != null)
        {
            if (account.getAccountId() != null)
            {
                account = getAccountByAccountId(account.getAccountId());
                if (course != null)
                {
                    if (course.getCourseId() != null)
                    {
                        course = courseService.getCourseByCourseId(course.getCourseId());

                        if (!account.getCourses().contains(course))
                        {
                            account.getCourses().add(course);

                            accountRepository.save(account);
                            return account;
                        }
                        else
                        {
                            throw new UpdateAccountException("Account with ID " + account.getAccountId() + " already contains Course with ID " + course.getCourseId());
                        }
                    }
                    else
                    {
                        throw new UpdateAccountException("Course ID cannot be null");
                    }
                }
                else
                {
                    throw new UpdateAccountException("Course cannot be null");
                }
            }
            else
            {
                throw new UpdateAccountException("Account ID cannot be null");
            }
        }
        else
        {
            throw new UpdateAccountException("Account cannot be null");
        }
    }

    @Override
    public Account addStudentAttemptToAccount(Account account, StudentAttempt studentAttempt) throws UpdateAccountException, AccountNotFoundException, StudentAttemptNotFoundException
    {
        if (account != null)
        {
            if (account.getAccountId() != null)
            {
                account = getAccountByAccountId(account.getAccountId());
                if (studentAttempt != null)
                {
                    if (studentAttempt.getStudentAttemptId() != null)
                    {
                        studentAttempt = studentAttemptService.getStudentAttemptByStudentAttemptId(studentAttempt.getStudentAttemptId());

                        if (!account.getStudentAttempts().contains(studentAttempt))
                        {
                            if (accountRepository.getNumberOfStudentAttemptsByStudentForQuiz(studentAttempt.getQuiz().getContentId(), account.getAccountId()) < studentAttempt.getQuiz().getMaxAttemptsPerStudent())
                            {
                                account.getStudentAttempts().add(studentAttempt);

                                accountRepository.save(account);
                                return account;
                            }
                            else
                            {
                                studentAttemptService.deleteStudentAttemptByStudentAttemptId(studentAttempt.getStudentAttemptId()); // Needs work
                                throw new UpdateAccountException("This account has ran out of attempts for quiz: " + studentAttempt.getQuiz().getName() + ". Attempt is thus deleted");
                            }
                        }
                        else
                        {
                            throw new UpdateAccountException("Account with ID " + account.getAccountId() + " already contains StudentAttempt with ID " + studentAttempt.getStudentAttemptId());
                        }
                    }
                    else
                    {
                        throw new UpdateAccountException("StudentAttempt ID cannot be null");
                    }
                }
                else
                {
                    throw new UpdateAccountException("StudentAttempt cannot be null");
                }
            }
            else
            {
                throw new UpdateAccountException("Account ID cannot be null");
            }
        }
        else
        {
            throw new UpdateAccountException("Account cannot be null");
        }
    }

//    @Override
//    public Account addStudentAttemptToAccount(Account account, StudentAttempt studentAttempt) throws AccountNotFoundException, StudentAttemptNotFoundException, UpdateAccountException
//    {
//        account = getAccountByAccountId(account.getAccountId());
//        studentAttempt = studentAttemptService.getStudentAttemptByStudentAttemptId(studentAttempt.getStudentAttemptId());
//
//        if (account.getStudentAttempts().contains(studentAttempt))
//        {
//            throw new UpdateAccountException("This attempt has already been recorded.");
//        }
//        else if (accountRepository.getNumberOfStudentAttemptsByStudentForQuiz(studentAttempt.getQuiz().getContentId(), account.getAccountId()) >= studentAttempt.getQuiz().getMaxAttemptsPerStudent())
//        {
//            studentAttemptRepository.delete(studentAttempt); // clean up
//            throw new UpdateAccountException("This account has ran out of attempts for quiz: " + studentAttempt.getQuiz().getName() + ". Attempt is thus deleted");
//        }
//        else
//        {
//            account.getStudentAttempts().add(studentAttempt);
//        }
//
//        accountRepository.saveAndFlush(account);
//        return account;
//    }

    @Override
    public Long deactivateAccount(Long deactivatingAccountId, Long requestingAccountId) throws AccountNotFoundException, AccountPermissionDeniedException
    {
        if (deactivatingAccountId == requestingAccountId)
        {
            throw new AccountPermissionDeniedException("You cannot deactivate your own account");
        }

        Account requestingAccount = getAccountByAccountId(requestingAccountId);
        Account deactivatingAccount = getAccountByAccountId(deactivatingAccountId);

        // Check that requestingAccount is an admin account
        if (requestingAccount.getIsAdmin())
        {
            deactivatingAccount.setIsActive(Boolean.FALSE);
            Account deactivatedAccount = accountRepository.saveAndFlush(deactivatingAccount);
            return deactivatedAccount.getAccountId();
        }
        else
        {
            throw new AccountPermissionDeniedException("You do not have administrative rights to deactivate accounts.");
        }
    }

    public Account login(String username, String password) throws InvalidLoginCredentialsException, AccountNotFoundException
    {
        if (username != null)
        {
            Account account = getAccountByUsername(username);
            String hashedPassword = CryptographicHelper.getSHA256Digest(password, account.getSalt());

            if (account.getPassword().equals(hashedPassword))
            {
                return account;
            }
            else
            {
                throw new InvalidLoginCredentialsException("Username or Password is invalid");
            }
        }
        else
        {
            throw new InvalidLoginCredentialsException("Username or Password is invalid");
        }
    }
}

