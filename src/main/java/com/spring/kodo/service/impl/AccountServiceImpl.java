package com.spring.kodo.service.impl;

import com.spring.kodo.entity.*;
import com.spring.kodo.repository.CourseRepository;
import com.spring.kodo.repository.ForumPostRepository;
import com.spring.kodo.repository.StudentAttemptRepository;
import com.spring.kodo.service.*;
import com.spring.kodo.util.MessageFormatterUtil;
import com.spring.kodo.util.cryptography.CryptographicHelper;
import com.spring.kodo.util.exception.*;
import com.spring.kodo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.*;
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
    public Account createNewAccount(Account newAccount, List<String> tagTitles) throws InputDataValidationException, UnknownPersistenceException, AccountUsernameOrEmailExistsException
    {
        try
        {
            Set<ConstraintViolation<Account>> constraintViolations = validator.validate(newAccount);
            if(constraintViolations.isEmpty())
            {
                // Persist Account
                Account persistedAccount = accountRepository.saveAndFlush(newAccount);

                // Process Tags
                if(tagTitles != null && (!tagTitles.isEmpty()))
                {
                    for(String tagTitle: tagTitles)
                    {
                        Tag tag = tagService.getTagByTitleOrCreateNew(tagTitle);
                        try
                        {
                            persistedAccount = addTagToAccount(persistedAccount, tag);
                        }
                        catch (TagNotFoundException | AccountNotFoundException | UpdateAccountException ex)
                        {
                            // Since we are still in a creation step, these generic exceptions will probably not happen / can kinda be ignored
                            // e.g. acc not found, duplicate tag exception
                            continue;
                        }
                    }
                }

                accountRepository.saveAndFlush(persistedAccount);
                return persistedAccount;
            }
            else
            {
                throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        catch(DataAccessException ex)
        {
            if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
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
    public List<Account> getAllAccounts()
    {
        List<Account> accounts = accountRepository.findAll();
        return accounts;
    }

    @Override
    public Account updateAccount(Account account,
                                 List<String> tagTitles,
                                 List<Long> enrolledCourseIds,
                                 List<Long> courseIds,
                                 List<Long> forumThreadIds,
                                 List<Long> forumPostIds,
                                 List<Long> studentAttemptIds)
            throws AccountNotFoundException, TagNotFoundException,
            InputDataValidationException, UpdateAccountException,
            EnrolledCourseNotFoundException, CourseNotFoundException,
            StudentAttemptNotFoundException, ForumThreadNotFoundException,
            ForumPostNotFoundException
    {

        if(account != null && account.getAccountId() != null)
        {
            Account accountToUpdate = null;
            Set<ConstraintViolation<Account>>constraintViolations = validator.validate(account);

            if(constraintViolations.isEmpty())
            {
                // Get managed instance of account to be updated
                accountToUpdate = getAccountByAccountId(account.getAccountId());

                if (accountToUpdate.getUsername().equals(account.getUsername()))
                {
                    // Update tags (interests) - Unidirectional
                    if(tagTitles != null)
                    {
                        accountToUpdate.getInterests().clear();
                        for(String tagTitle: tagTitles)
                        {
                            Tag tag = tagService.getTagByTitleOrCreateNew(tagTitle);
                            addTagToAccount(accountToUpdate, tag);
                        }
                    }

                    // Update enrolled courses - Unidirectional
                    if (enrolledCourseIds != null)
                    {
                        accountToUpdate.getEnrolledCourses().clear();
                        for (Long enrolledCourseId: enrolledCourseIds)
                        {
                            EnrolledCourse enrolledCourse = enrolledCourseService.getEnrolledCourseByEnrolledCourseId(enrolledCourseId);
                            addEnrolledCourseToAccount(accountToUpdate, enrolledCourse);
                        }
                    }

                    // Update courses (as a tutor) - Bidirectional, 1-to-many
                    if(courseIds != null)
                    {
                        for(Course course: accountToUpdate.getCourses())
                        {
                            course.setTutor(null);
                        }

                        accountToUpdate.getCourses().clear();
                        for(Long courseId: courseIds)
                        {
                            Course course = courseService.getCourseByCourseId(courseId);
                            addCourseToAccount(accountToUpdate, course);
                        }
                    }

                    // Update forumThreads - Unidirectional
                    if (forumThreadIds != null)
                    {
                        accountToUpdate.getForumThreads().clear();
                        for (Long forumThreadId: forumThreadIds)
                        {
                            ForumThread forumThread = forumThreadService.getForumThreadByForumThreadId(forumThreadId);
                            addForumThreadToAccount(accountToUpdate, forumThread);
                        }
                    }

                    // Update forumPosts (as an account) - Bidirectional
                    if(forumPostIds != null)
                    {
                        for(ForumPost forumPost: accountToUpdate.getForumPosts())
                        {
                            forumPost.setAccount(null);
                        }

                        accountToUpdate.getForumPosts().clear();
                        for(Long forumPostId: forumPostIds)
                        {
                            ForumPost forumPost = forumPostService.getForumPostByForumPostId(forumPostId);
                            addForumPostToAccount(accountToUpdate, forumPost);
                        }
                    }

                    // Update studentAttempts - Unidirectional
                    if (studentAttemptIds != null)
                    {
                        accountToUpdate.getStudentAttempts().clear();
                        for (Long studentAttemptId: studentAttemptIds)
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
    public Account addTagToAccount(Account account, Tag tag) throws AccountNotFoundException, TagNotFoundException, UpdateAccountException {

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
    public Account addEnrolledCourseToAccount(Account account, EnrolledCourse enrolledCourse) throws AccountNotFoundException, EnrolledCourseNotFoundException, UpdateAccountException {

        account = getAccountByAccountId(account.getAccountId());
        enrolledCourse = enrolledCourseService.getEnrolledCourseByEnrolledCourseId(enrolledCourse.getEnrolledCourseId());

        if (!account.getEnrolledCourses().contains(enrolledCourse))
        {
            account.getEnrolledCourses().add(enrolledCourse);
        }
        else
        {
            throw new UpdateAccountException("Unable to add enrolled course for course: " + enrolledCourse.getParentCourse() +
                    " to account with ID: " + account.getAccountId() + " as this account is already enrolled in this course");
        }

        accountRepository.saveAndFlush(account);
        return account;
    }

    @Override
    public Account addCourseToAccount(Account account, Course course) throws AccountNotFoundException, UpdateAccountException, CourseNotFoundException {

        // BIDRECTIONAL!!!
        account = getAccountByAccountId(account.getAccountId());
        course = courseService.getCourseByCourseId(course.getCourseId());

        if (!account.getCourses().contains(course))
        {
            account.getCourses().add(course);
            course.setTutor(account);
        }
        else
        {
            throw new UpdateAccountException("Unable to add course: " + course.getName() +
                    " to tutor with account ID: " + account.getAccountId() + " as this account is already the tutor of this course");
        }

        accountRepository.saveAndFlush(account);
        courseRepository.saveAndFlush(course);

        return account;
    }

    @Override
    public Account addForumThreadToAccount(Account account, ForumThread forumThread) throws AccountNotFoundException, UpdateAccountException, ForumThreadNotFoundException
    {
        account = getAccountByAccountId(account.getAccountId());
        forumThread = forumThreadService.getForumThreadByForumThreadId(forumThread.getForumThreadId());

        if (!account.getForumThreads().contains(forumThread))
        {
            account.getForumThreads().add(forumThread);
        }
        else
        {
            throw new UpdateAccountException("Forum thread is already associated to account");
        }

        accountRepository.saveAndFlush(account);
        return account;
    }

    @Override
    public Account addForumPostToAccount(Account account, ForumPost forumPost) throws AccountNotFoundException, ForumPostNotFoundException, UpdateAccountException
    {
        account = getAccountByAccountId(account.getAccountId());
        forumPost = forumPostService.getForumPostByForumPostId(forumPost.getForumPostId());

        if (!account.getForumPosts().contains(forumPost))
        {
            account.getForumPosts().add(forumPost);
            forumPost.setAccount(account);
        }
        else
        {
            throw new UpdateAccountException("Unable to add forumPost: " + forumPost.getForumPostId() +
                    " to account account ID: " + account.getAccountId() + " as this account is already the creator of this post");
        }

        accountRepository.saveAndFlush(account);
        forumPostRepository.saveAndFlush(forumPost);

        return account;
    }

    @Override
    public Account addStudentAttemptToAccount (Account account, StudentAttempt studentAttempt) throws AccountNotFoundException, StudentAttemptNotFoundException, UpdateAccountException {

        account = getAccountByAccountId(account.getAccountId());
        studentAttempt = studentAttemptService.getStudentAttemptByStudentAttemptId(studentAttempt.getStudentAttemptId());

        if (account.getStudentAttempts().contains(studentAttempt))
        {
            throw new UpdateAccountException("This attempt has already been recorded.");
        }
        else if (accountRepository.getNumberOfStudentAttemptsByStudentForQuiz(studentAttempt.getQuiz().getContentId(), account.getAccountId()) >= studentAttempt.getQuiz().getMaxAttemptsPerStudent())
        {
            studentAttemptRepository.delete(studentAttempt); // clean up
            throw new UpdateAccountException("This account has ran out of attempts for quiz: " + studentAttempt.getQuiz().getName() + ". Attempt is thus deleted");
        }
        else
        {
            account.getStudentAttempts().add(studentAttempt);
        }

        accountRepository.saveAndFlush(account);
        return account;
    }

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

