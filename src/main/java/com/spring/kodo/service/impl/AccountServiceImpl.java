package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Account;
import com.spring.kodo.repository.AccountRepository;
import com.spring.kodo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService
{
    @Autowired // With this annotation, we do not to populate AccountRepository in this class' constructor
    private AccountRepository accountRepository;

    public List<Account> getAccounts()
    {
        return this.accountRepository.findAll();
    }
}

