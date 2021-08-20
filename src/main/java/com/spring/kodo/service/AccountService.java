package com.spring.kodo.service;

import com.spring.kodo.entity.Account;
import java.util.List;
import java.util.Optional;

public interface AccountService
{
    Optional<Account> getAccountByAccountId(Long accountId);
    List<Account> getAccounts();
}
