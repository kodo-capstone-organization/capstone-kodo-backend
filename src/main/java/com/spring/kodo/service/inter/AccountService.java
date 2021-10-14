package com.spring.kodo.service.inter;

import com.spring.kodo.entity.*;
import com.spring.kodo.util.exception.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AccountService
{
    public Account createNewAccount(Account newAccount)
            throws
            AccountUsernameExistException,
            AccountEmailExistException,
            InputDataValidationException,
            UnknownPersistenceException;

    Account createNewAccount(Account newAccount, List<String> tagTitles)
            throws
            AccountUsernameExistException,
            AccountEmailExistException,
            TagNameExistsException,
            TagNotFoundException,
            AccountNotFoundException,
            UpdateAccountException,
            InputDataValidationException,
            UnknownPersistenceException;

    Account createNewAccount(Account newAccount, List<String> tagTitles, MultipartFile displayPicture)
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
            CreateNewAccountException;

    Account getAccountByAccountId(Long accountId) throws AccountNotFoundException;

    Account getAccountByUsername(String username) throws AccountNotFoundException;

    Account getAccountByEmail(String email) throws AccountNotFoundException;

    Account getAccountByCourseId(Long courseId) throws AccountNotFoundException;

    Account getAccountByLessonId(Long lessonId) throws AccountNotFoundException;

    Account getAccountByEnrolledCourseId(Long enrolledCourseId) throws AccountNotFoundException;

    Account getAccountByEnrolledLessonId(Long enrolledLessonId) throws AccountNotFoundException;

    Account getAccountByEnrolledContentId(Long enrolledContentId) throws AccountNotFoundException;

    Account getAccountByStudentAttemptId(Long studentAttemptId) throws AccountNotFoundException;

    Account getAccountByQuizId(Long quizId) throws AccountNotFoundException;

    List<Account> getAllAccounts();

    List<Account> getAllAccountsByTagId(Long tagId);

    List<Account> getSomeAccountsBySomeAccountIds(List<Long> accountIds) throws AccountNotFoundException;

    Account updateAccount(Account account) throws UpdateAccountException, AccountNotFoundException, AccountEmailExistException;

    Account updateAccount(
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
            StudentAttemptNotFoundException;

    Account updateAccountPassword(Long accountId, String username, String oldPassword, String newPassword) throws UpdateAccountException, AccountNotFoundException, InputDataValidationException;

    Account addTagToAccount(Account account, Tag tag) throws AccountNotFoundException, TagNotFoundException, UpdateAccountException;

    Account addEnrolledCourseToAccount(Account account, EnrolledCourse enrolledCourse) throws UpdateAccountException, AccountNotFoundException, EnrolledCourseNotFoundException;

    Account addCourseToAccount(Account account, Course course) throws UpdateAccountException, AccountNotFoundException, CourseNotFoundException;

    Boolean isAccountExistsByUsername(String username);

    Boolean isAccountExistsByEmail(String email);

    Long reactivateAccount(Long reactivatingAccountId, Long requestingAccountId) throws AccountNotFoundException, AccountPermissionDeniedException;

    Long deactivateAccount(Long deactivatingAccountId, Long requestingAccountId) throws AccountNotFoundException, AccountPermissionDeniedException;

    Account userLogin(String username, String password) throws InvalidLoginCredentialsException;

    Account adminLogin(String username, String password) throws InvalidLoginCredentialsException;

    Account upgradeAdminStatus(Long accountId) throws AccountNotFoundException;

    Account downgradeAdminStatus(Long accountId) throws AccountNotFoundException;

    Integer getTotalEnrollmentCountByAccountId(Long requestingAccountId) throws AccountNotFoundException;

    Integer getTotalPublishedCourseCountByAccountId(Long requestingAccountId) throws AccountNotFoundException;

    Integer getTotalCourseCountByAccountId(Long requestingAccountId) throws AccountNotFoundException;

    Integer getNumCoursesTaught(Long tutorId) throws AccountNotFoundException;

    Integer getNumCoursesCreatedCurrentMonth(Long tutorId) throws AccountNotFoundException;

    Integer getNumCoursesCreatedLastMonth(Long tutorId) throws AccountNotFoundException;

    Integer getCurrentMonthNumberOfAccountCreation();

    Integer getPreviousMonthNumberOfAccountCreation();
}
