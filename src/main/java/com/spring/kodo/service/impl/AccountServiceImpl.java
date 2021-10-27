package com.spring.kodo.service.impl;

import com.spring.kodo.entity.*;
import com.spring.kodo.repository.AccountRepository;
import com.spring.kodo.service.inter.*;
import com.spring.kodo.util.helper.FormatterHelper;
import com.spring.kodo.util.helper.CryptographicHelper;
import com.spring.kodo.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class AccountServiceImpl implements AccountService
{
    @Autowired // With this annotation, we do not to populate AccountRepository in this class' constructor
    private AccountRepository accountRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private TagService tagService;

    @Autowired
    private EnrolledCourseService enrolledCourseService;

    @Autowired
    private CourseService courseService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public AccountServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Account createNewAccount(Account newAccount)
            throws
            AccountUsernameExistException,
            AccountEmailExistException,
            InputDataValidationException,
            UnknownPersistenceException
    {
        try
        {
            Set<ConstraintViolation<Account>> constraintViolations = validator.validate(newAccount);
            if (constraintViolations.isEmpty())
            {
                if (!isAccountExistsByUsername(newAccount.getUsername()))
                {
                    if (!isAccountExistsByEmail(newAccount.getEmail()))
                    {
                        // Persist Account
                        accountRepository.saveAndFlush(newAccount);
                        return newAccount;
                    }
                    else
                    {
                        throw new AccountEmailExistException("Account with email " + newAccount.getEmail() + " already exists!");
                    }
                }
                else
                {
                    throw new AccountUsernameExistException("Account with username " + newAccount.getUsername() + " already exists!");
                }
            }
            else
            {
                throw new InputDataValidationException(FormatterHelper.prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        catch (DataAccessException ex)
        {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }


    @Override
    public Account createNewAccount(Account newAccount, List<String> tagTitles)
            throws
            AccountUsernameExistException,
            AccountEmailExistException,
            TagNameExistsException,
            TagNotFoundException,
            AccountNotFoundException,
            UpdateAccountException,
            InputDataValidationException,
            UnknownPersistenceException
    {
        newAccount = createNewAccount(newAccount);

        // Process Tags
        if (tagTitles != null && (!tagTitles.isEmpty()))
        {
            for (String tagTitle : tagTitles)
            {
                Tag tag = tagService.getTagByTitleOrCreateNew(tagTitle);
                newAccount = addTagToAccount(newAccount, tag);
            }
        }

        return newAccount;
    }

    @Override
    public Account createNewAccount(Account newAccount, List<String> tagTitles, MultipartFile displayPicture)
            throws
            AccountUsernameExistException,
            AccountEmailExistException,
            TagNameExistsException,
            TagNotFoundException,
            AccountNotFoundException,
            UpdateAccountException,
            InputDataValidationException,
            UnknownPersistenceException,
            FileUploadToGCSException,
            CreateNewAccountException
    {
        newAccount = createNewAccount(newAccount, tagTitles);

        if (displayPicture != null)
        {
            String displayPictureUrl = fileService.upload(displayPicture);
            newAccount.setDisplayPictureUrl(displayPictureUrl);
        }
        else
        {
            throw new CreateNewAccountException("Display picture not provided for account to be create");
        }

        return newAccount;
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
    public Account getAccountByLessonId(Long lessonId) throws AccountNotFoundException
    {
        Account account = accountRepository.findByLessonId(lessonId).orElse(null);

        if (account != null)
        {
            return account;
        }
        else
        {
            throw new AccountNotFoundException("Account with Lesson ID: " + lessonId + " does not exist!");
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
    public Account getAccountByEnrolledLessonId(Long enrolledLessonId) throws AccountNotFoundException
    {
        Account account = accountRepository.findByEnrolledLessonId(enrolledLessonId).orElse(null);

        if (account != null)
        {
            return account;
        }
        else
        {
            throw new AccountNotFoundException("Account with EnrolledLesson ID: " + enrolledLessonId + " does not exist!");
        }
    }

    @Override
    public Account getAccountByEnrolledContentId(Long enrolledContentId) throws AccountNotFoundException
    {
        Account account = accountRepository.findByEnrolledContentId(enrolledContentId).orElse(null);

        if (account != null)
        {
            return account;
        }
        else
        {
            throw new AccountNotFoundException("Account with EnrolledContent ID: " + enrolledContentId + " does not exist!");
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
    public Account getAccountByQuizId(Long quizId) throws AccountNotFoundException
    {
        Account account = accountRepository.findByQuizId(quizId).orElse(null);

        if (account != null)
        {
            return account;
        }
        else
        {
            throw new AccountNotFoundException("Account with Quiz ID: " + quizId + " does not exist!");
        }
    }

    @Override
    public List<Account> getAllAccounts()
    {
        List<Account> accounts = accountRepository.findAll();

        return accounts;
    }

    @Override
    public List<Account> getAllAccountsByTagId(Long tagId)
    {
        List<Account> accounts = accountRepository.findAllAccountsByTagId(tagId);

        return accounts;
    }

    @Override
    public List<Account> getSomeAccountsBySomeAccountIds(List<Long> accountIds) throws AccountNotFoundException
    {
        List<Account> accounts = new ArrayList<>();

        for (Long accountId: accountIds)
        {
            Account retrievedAccount = getAccountByAccountId(accountId);
            accounts.add(retrievedAccount);
        }

        return accounts;
    }

    @Override
    public Account downgradeAdminStatus(Long accountId) throws AccountNotFoundException {
        if (accountId != null) {
            Account accountToUpdate = getAccountByAccountId(accountId);
            accountToUpdate.setIsAdmin(false);
            accountToUpdate = accountRepository.saveAndFlush(accountToUpdate);
            return accountToUpdate;
        } else {
            throw new AccountNotFoundException("Account with id " + accountId + " cannot be found");
        }
    }

    @Override
    public Account upgradeAdminStatus(Long accountId) throws AccountNotFoundException {
        if (accountId != null) {
            Account accountToUpdate = getAccountByAccountId(accountId);
            accountToUpdate.setIsAdmin(true);
            accountToUpdate = accountRepository.saveAndFlush(accountToUpdate);
            return accountToUpdate;
        } else {
            throw new AccountNotFoundException("Account with id " + accountId + " cannot be found");
        }
    }

    @Override
    public Account updateAccount(Account account) throws UpdateAccountException, AccountNotFoundException, AccountEmailExistException
    {
        if (account != null)
        {
            if (account.getAccountId() != null)
            {
                Account accountToUpdate = getAccountByAccountId(account.getAccountId());

                if (account.getUsername().equals(accountToUpdate.getUsername()))
                {
                    if (!account.getEmail().equals(accountToUpdate.getEmail()))
                    {
                        if (!isAccountExistsByEmail(account.getEmail()))
                        {
                            // Update Non-Relational Fields
                            accountToUpdate.setEmail(account.getEmail());
                        }
                        else
                        {
                            throw new AccountEmailExistException("Account with email " + account.getEmail() + " already exists!");
                        }
                    }

                    // Update Non-Relational Fields
                    accountToUpdate.setName(account.getName());
                    accountToUpdate.setBio(account.getBio());
                    accountToUpdate.setEmail(account.getEmail());
                    accountToUpdate.setDisplayPictureUrl(account.getDisplayPictureUrl());
                    accountToUpdate.setIsAdmin(account.getIsAdmin());
                    accountToUpdate.setIsActive(account.getIsActive());
                    accountToUpdate.setStripeAccountId(account.getStripeAccountId());

                    accountToUpdate = accountRepository.saveAndFlush(accountToUpdate);
                    return accountToUpdate;
                }
                else
                {
                    throw new UpdateAccountException("Account username does not match any existing account record");
                }
            }
            else
            {
                throw new UpdateAccountException("Account ID not provided for account to be updated");
            }
        }
        else
        {
            throw new UpdateAccountException("Account not provided for account to be updated");
        }
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
    ) throws UpdateAccountException,
            AccountNotFoundException,
            InputDataValidationException,
            AccountEmailExistException,
            TagNameExistsException,
            UnknownPersistenceException,
            TagNotFoundException,
            EnrolledCourseNotFoundException,
            StudentAttemptNotFoundException
    {
        Account accountToUpdate = updateAccount(account);

        // Update Tags (interests) - Unidirectional
        if (tagTitles != null)
        {
            accountToUpdate.getInterests().clear();
            for (String tagTitle : tagTitles)
            {
                Tag tag = tagService.getTagByTitleOrCreateNew(tagTitle);
                addTagToAccount(accountToUpdate, tag);
            }
        }
        else
        {
            throw new UpdateAccountException("Tag titles not provided for account to be updated");
        }

        // Update EnrolledCourses - Unidirectional
        if (enrolledCourseIds != null)
        {
            accountToUpdate.getEnrolledCourses().clear();
            for (Long enrolledCourseId : enrolledCourseIds)
            {
                EnrolledCourse enrolledCourse = enrolledCourseService.getEnrolledCourseByEnrolledCourseId(enrolledCourseId);
                addEnrolledCourseToAccount(accountToUpdate, enrolledCourse);
            }
        }
        else
        {
            throw new UpdateAccountException("EnrolledCourse IDs not provided for account to be updated");
        }

        accountToUpdate = accountRepository.saveAndFlush(accountToUpdate);
        return accountToUpdate;
    }

    @Override
    public Account updateAccountPassword(Long accountId, String username, String oldPassword, String newPassword) throws UpdateAccountException, AccountNotFoundException, InputDataValidationException
    {
        if (username != null)
        {
            if (oldPassword != null)
            {
                if (newPassword != null)
                {
                    Account account = getAccountByAccountId(accountId);

                    if (account.getUsername().equals(username))
                    {
                        String salt = account.getSalt();
                        String oldHashedPassword = CryptographicHelper.getSHA256Digest(oldPassword, salt);
                        String storedHashedPassword = account.getPassword();

                        if (oldHashedPassword.equals(storedHashedPassword))
                        {
                            if (!oldPassword.equals(newPassword))
                            {
                                account.setPassword(newPassword);

                                accountRepository.saveAndFlush(account);
                                return account;
                            }
                            else
                            {
                                throw new UpdateAccountException("Old password cannot be the same as New password");
                            }
                        }
                        else
                        {
                            throw new UpdateAccountException("Old password does not match existing account record");
                        }
                    }
                    else
                    {
                        throw new UpdateAccountException("Username does not match any existing account record");
                    }
                }
                else
                {
                    throw new UpdateAccountException("New Password not provided for account to be updated");
                }
            }
            else
            {
                throw new UpdateAccountException("Old Password not provided for account to be updated");
            }
        }
        else
        {
            throw new UpdateAccountException("Username not provided for account to be updated");

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
    public Boolean isAccountExistsByUsername(String username)
    {
        return accountRepository.existsByUsername(username);
    }

    @Override
    public Boolean isAccountExistsByEmail(String email)
    {
        return accountRepository.existsByEmail(email);
    }

    @Override
    public Long reactivateAccount(Long reactivatingAccountId, Long requestingAccountId) throws AccountNotFoundException, AccountPermissionDeniedException
    {
        Account requestingAccount = getAccountByAccountId(requestingAccountId);
        Account reactivatingAccount = getAccountByAccountId(reactivatingAccountId);

        if (reactivatingAccountId == requestingAccountId)
        {
//            throw new AccountPermissionDeniedException("You cannot deactivate your own account");
            reactivatingAccount.setIsActive(Boolean.TRUE);
            Account deactivatedAccount = accountRepository.saveAndFlush(reactivatingAccount);
            return deactivatedAccount.getAccountId();
        }
        // Check that requestingAccount is an admin account
        else if (requestingAccount.getIsAdmin())
        {
            reactivatingAccount.setIsActive(Boolean.TRUE);
            Account deactivatedAccount = accountRepository.saveAndFlush(reactivatingAccount);
            return deactivatedAccount.getAccountId();
        }
        else
        {
            throw new AccountPermissionDeniedException("You do not have the rights to deactivate accounts.");
        }
    }

    @Override
    public Long deactivateAccount(Long deactivatingAccountId, Long requestingAccountId) throws AccountNotFoundException, AccountPermissionDeniedException
    {
        Account requestingAccount = getAccountByAccountId(requestingAccountId);
        Account deactivatingAccount = getAccountByAccountId(deactivatingAccountId);

        if (deactivatingAccountId == requestingAccountId)
        {
//            throw new AccountPermissionDeniedException("You cannot deactivate your own account");
            deactivatingAccount.setIsActive(Boolean.FALSE);
            Account deactivatedAccount = accountRepository.saveAndFlush(deactivatingAccount);
            return deactivatedAccount.getAccountId();
        }
        // Check that requestingAccount is an admin account
        else if (requestingAccount.getIsAdmin())
        {
            deactivatingAccount.setIsActive(Boolean.FALSE);
            Account deactivatedAccount = accountRepository.saveAndFlush(deactivatingAccount);
            return deactivatedAccount.getAccountId();
        }
        else
        {
            throw new AccountPermissionDeniedException("You do not have the rights to deactivate accounts.");
        }
    }

    @Override
    public Account userLogin(String username, String password) throws InvalidLoginCredentialsException
    {
        if (username != null)
        {
            try
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
            catch (AccountNotFoundException ex)
            {
                throw new InvalidLoginCredentialsException("Username or Password is invalid");
            }
        }
        else
        {
            throw new InvalidLoginCredentialsException("Username or Password is invalid");
        }
    }

    @Override
    public Account adminLogin(String username, String password) throws InvalidLoginCredentialsException
    {
        if (username != null)
        {
            try
            {
                Account account = getAccountByUsername(username);

                if (account.getIsAdmin())
                {
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
            catch (AccountNotFoundException ex)
            {
                throw new InvalidLoginCredentialsException("Username or Password is invalid");
            }
        }
        else
        {
            throw new InvalidLoginCredentialsException("Username or Password is invalid");
        }
    }

    @Override
    public Integer getTotalEnrollmentCountByAccountId(Long requestingAccountId) throws AccountNotFoundException
    {
        Account requestingAccount = getAccountByAccountId(requestingAccountId);
        return this.accountRepository.getTotalEnrollmentCountByAccountId(requestingAccount.getAccountId()).orElse(0);
    }

    @Override
    public Integer getTotalPublishedCourseCountByAccountId(Long requestingAccountId) throws AccountNotFoundException
    {
        Account requestingAccount = getAccountByAccountId(requestingAccountId);
        return this.accountRepository.getTotalPublishedCourseCountByAccountId(requestingAccount.getAccountId()).orElse(0);
    }

    @Override
    public Integer getTotalCourseCountByAccountId(Long requestingAccountId) throws AccountNotFoundException
    {
        Account requestingAccount = getAccountByAccountId(requestingAccountId);
        return this.accountRepository.getTotalCourseCountByAccountId(requestingAccount.getAccountId()).orElse(0);
    }

    @Override
    public Integer getNumCoursesTaught(Long tutorId) throws AccountNotFoundException
    {
        Account tutor = getAccountByAccountId(tutorId);
        return this.accountRepository.findNumCoursesTaught(tutorId).orElse(0);
    }

    @Override
    public Integer getNumCoursesCreatedCurrentMonth(Long tutorId) throws AccountNotFoundException
    {
        Account tutor = getAccountByAccountId(tutorId);
        return this.accountRepository.findNumCoursesCreatedCurrentMonth(tutorId).orElse(0);
    }

    @Override
   public Integer getNumCoursesCreatedLastMonth(Long tutorId) throws AccountNotFoundException
   {
       Account tutor = getAccountByAccountId(tutorId);
       return this.accountRepository.findNumCoursesCreatedLastMonth(tutorId).orElse(0);
   }

   @Override
   public Integer getCurrentMonthNumberOfAccountCreation()
   {
       return this.accountRepository.getCurrentMonthNumberOfAccountCreation().orElse(0);
   }

   @Override
   public Integer getPreviousMonthNumberOfAccountCreation()
   {
       return this.accountRepository.getPreviousMonthNumberOfAccountCreation().orElse(0);
   }
}

