package com.spring.kodo.service;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface AccountService
{
    Account createNewAccount(Account account);
    List<Account> createNewAccounts(List<Account> accounts);

    Optional<Account> getAccountByAccountId(Long accountId);
    Optional<Account> getAccountByUsername(String username);
    Optional<Account> getAccountByName(String name);
    Optional<Account> getAccountByEmail(String email);
    List<Account> getAccounts();

    Account addTagToAccount(Account account, Tag tag);
}
