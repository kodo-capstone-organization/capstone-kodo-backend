package com.spring.kodo.service;

import com.spring.kodo.entity.*;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface AccountService
{
    Account createNewAccount(Account account, List<String> tagTitles) throws TagNameExistsException, InputDataValidationException, UnknownPersistenceException, AccountUsernameOrEmailExistsException;

    List<Account> getAllAccounts();

    Account getAccountByAccountId(Long accountId) throws AccountNotFoundException;

    Account getAccountByUsername(String username) throws AccountNotFoundException;

    Account getAccountByEmail(String email) throws AccountNotFoundException;

    Account getAccountByCourseId(Long courseId) throws AccountNotFoundException;

    Account updateAccount(
            Account account,
            List<String> tagTitles,
            List<Long> enrolledCourseIds,
            List<Long> coursesIds,
            List<Long> forumThreadIds,
            List<Long> forumPostIds,
            List<Long> studentAttemptIds
    )
            throws AccountNotFoundException, TagNotFoundException,
            UpdateAccountException, EnrolledCourseNotFoundException,
            CourseNotFoundException, StudentAttemptNotFoundException,
            ForumThreadNotFoundException, ForumPostNotFoundException,
            TagNameExistsException, UnknownPersistenceException,
            InputDataValidationException;

    Account addTagToAccount(Account account, Tag tag) throws AccountNotFoundException, TagNotFoundException, UpdateAccountException;

    Account addEnrolledCourseToAccount(Account account, EnrolledCourse enrolledCourse) throws UpdateAccountException, AccountNotFoundException, EnrolledCourseNotFoundException;

    Account addCourseToAccount(Account account, Course course) throws UpdateAccountException, AccountNotFoundException, CourseNotFoundException;

    Account addForumThreadToAccount(Account account, ForumThread forumThread) throws UpdateAccountException, AccountNotFoundException, ForumThreadNotFoundException;

    Account addForumPostToAccount(Account account, ForumPost forumPost) throws UpdateAccountException, AccountNotFoundException, ForumPostNotFoundException;

    Account addStudentAttemptToAccount(Account account, StudentAttempt studentAttempt) throws UpdateAccountException, AccountNotFoundException, StudentAttemptNotFoundException;

    Long deactivateAccount(Long deactivatingAccountId, Long requestingAccountId) throws AccountNotFoundException, AccountPermissionDeniedException;

    Account login(String username, String password) throws InvalidLoginCredentialsException, AccountNotFoundException;
}
