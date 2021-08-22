package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Account;
import com.spring.kodo.repository.AccountRepository;
import com.spring.kodo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService
{
    @Autowired // With this annotation, we do not to populate AccountRepository in this class' constructor
    private AccountRepository accountRepository;

    @Override
    public Account createNewAccount(Account account)
    {
        return this.accountRepository.save(account);
    }

    @Override
    public Optional<Account> getAccountByAccountId(Long accountId)
    {
        return this.accountRepository.findById(accountId);
    }

    @Override
    public Optional<Account> getAccountByUsername(String username)
    {
        return this.accountRepository.findByUsername(username);
    }

    @Override
    public Optional<Account> getAccountByName(String name)
    {
        return this.accountRepository.findByName(name);
    }

    @Override
    public Optional<Account> getAccountByEmail(String email)
    {
        return this.accountRepository.findByEmail(email);
    }

    @Override
    public List<Account> getAccounts()
    {
        return this.accountRepository.findAll();
    }
}

