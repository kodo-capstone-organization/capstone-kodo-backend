package com.spring.kodo.service;

import com.spring.kodo.entity.*;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface AccountService
{
    Account createNewAccount(Account account, List<String> tagTitles) throws InputDataValidationException, AccountExistsException;

    List<Account> getAllAccounts();

    Account getAccountByAccountId(Long accountId) throws AccountNotFoundException;

    Account getAccountByUsername(String username) throws AccountNotFoundException;

    Account getAccountByEmail(String email) throws AccountNotFoundException;

    Account updateAccount(Account account,
                          List<String> tagTitles,
                          List<Long> enrolledCourseIds,
                          List<Long> coursesIds,
                          List<Long> forumThreadIds,
                          List<Long> forumPostIds,
                          List<Long> studentAttemptIds)
            throws AccountNotFoundException, TagNotFoundException, InputDataValidationException, UpdateAccountException, EnrolledCourseNotFoundException, CourseNotFoundException, StudentAttemptQuestionNotFoundException, StudentAttemptNotFoundException;

    Account addTagToAccount(Account account, Tag tag) throws AccountNotFoundException, TagNotFoundException, UpdateAccountException;

    Account addEnrolledCourseToAccount(Account account, EnrolledCourse enrolledCourse) throws AccountNotFoundException, TagNotFoundException, UpdateAccountException, EnrolledCourseNotFoundException;

    Account addCourseToAccount(Account account, Course course) throws AccountNotFoundException, UpdateAccountException, CourseNotFoundException;

    Account addStudentAttemptToAccount(Account account, StudentAttempt studentAttempt) throws AccountNotFoundException, StudentAttemptNotFoundException, UpdateAccountException;

    Long deactivateAccount(Long deactivatingAccountId, Long requestingAccountId) throws AccountNotFoundException, AccountPermissionDeniedException;

    Account login(String username, String password) throws InvalidLoginCredentialsException, AccountNotFoundException;
}
