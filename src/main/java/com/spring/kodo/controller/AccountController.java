package com.spring.kodo.controller;

import com.spring.kodo.entity.Account;
import com.spring.kodo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/account")
public class AccountController
{
    @Autowired
    private final AccountService accountService;

    public AccountController(AccountService accountService)
    {
        this.accountService = accountService;
    }

    @GetMapping
    public List<Account> getAccounts()
    {
        return this.accountService.getAccounts();
    }
}
