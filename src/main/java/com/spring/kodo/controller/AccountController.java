package com.spring.kodo.controller;

import com.spring.kodo.entity.Account;
import com.spring.kodo.exception.AccountNotFoundException;
import com.spring.kodo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/account")
public class AccountController
{
    @Autowired
    private AccountService accountService;

    @GetMapping("/getAccountByAccountId/{accountId}")
    public Account getAccountByAccountId(@PathVariable Long accountId)
    {
        return this.accountService
                .getAccountByAccountId(accountId)
                .orElseThrow(AccountNotFoundException::new);
    }

    @GetMapping("/getAllAccounts")
    public List<Account> getAllAccounts()
    {
        return this.accountService.getAccounts();
    }
}
