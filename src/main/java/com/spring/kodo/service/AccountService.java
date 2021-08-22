package com.spring.kodo.service;

import com.spring.kodo.entity.Account;
import java.util.List;
import java.util.Optional;

public interface AccountService
{
    Account createNewAccount(Account account);

    Optional<Account> getAccountByAccountId(Long accountId);
    Optional<Account> getAccountByUsername(String username);
    Optional<Account> getAccountByName(String name);
    Optional<Account> getAccountByEmail(String email);
    List<Account> getAccounts();
}
