package com.spring.kodo.service;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Tag;
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
            throws AccountNotFoundException, TagNotFoundException, InputDataValidationException, UpdateAccountException;

    Account addTagToAccount(Account account, Tag tag) throws AccountNotFoundException, TagNotFoundException, UpdateAccountException;

    Long deactivateAccount(Long deactivatingAccountId, Long requestingAccountId) throws AccountNotFoundException, AccountPermissionDeniedException;

    Account login(String username, String password) throws InvalidLoginCredentialsException, AccountNotFoundException;
}
