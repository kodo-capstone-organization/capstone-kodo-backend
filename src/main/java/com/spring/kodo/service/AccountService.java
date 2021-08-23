package com.spring.kodo.service;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Tag;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface AccountService
{
    Account createNewAccount(Account account, List<String> tagTitles) throws InputDataValidationException;
    List<Account> createNewAccounts(List<Account> accounts);

    List<Account> getAllAccounts();
    Account getAccountByAccountId(Long accountId) throws AccountNotFoundException;
    Account getAccountByUsername(String username) throws AccountNotFoundException;
    Account getAccountByEmail(String email) throws AccountNotFoundException;

    Account addTagToAccount(Account account, Tag tag) throws AccountNotFoundException, TagNotFoundException, UpdateAccountException;

    Long deactivateAccount(Long deactivatingAccountId, Long requestingAccountId) throws AccountNotFoundException, AccountPermissionDeniedException;

    Account login(String username, String password) throws InvalidLoginCredentialsException, AccountNotFoundException;
}
