package com.spring.kodo.controller;

import com.spring.kodo.entity.Account;
import com.spring.kodo.util.exception.AccountNotFoundException;
import com.spring.kodo.service.AccountService;
import com.spring.kodo.util.exception.AccountPermissionDeniedException;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.InvalidLoginCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = "/account")
public class AccountController
{
    @Autowired
    private AccountService accountService;

    @GetMapping("/getAllAccounts")
    public List<Account> getAllAccounts()
    {
        return this.accountService.getAllAccounts();
    }

    @GetMapping("/getAccountByAccountId/{accountId}")
    public Account getAccountByAccountId(@PathVariable Long accountId)
    {
        try
        {
            Account account = this.accountService.getAccountByAccountId(accountId);
            return account;
        }
        catch (AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping("/createNewAccount")
    public Account createNewAccount(@RequestParam("account") Account newAccount, @RequestParam("tagTitles") List<String> tagTitles)
    {
        try
        {
            return this.accountService.createNewAccount(newAccount, tagTitles);
        }
        catch (InputDataValidationException ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @DeleteMapping("/deactivateAccount/{deactivatingAccountId}&{requestingAccountId}")
    public ResponseEntity deactivateAccount(@PathVariable Long deactivatingAccountId, @PathVariable Long requestingAccountId)
    {
        try
        {
            Long deactivatedAccountId = this.accountService.deactivateAccount(deactivatingAccountId, requestingAccountId);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deactivated account with Account ID: " + deactivatedAccountId);
        }
        catch (AccountPermissionDeniedException ex)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage());
        }
        catch (AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping("/login/{username}&{password}")
    public ResponseEntity login(@PathVariable String username, @PathVariable String password)
    {
        try
        {
            Account accountLoggedIn = this.accountService.login(username, password);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully login with Account ID: " + accountLoggedIn.getAccountId());
        }
        catch (AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        catch (InvalidLoginCredentialsException ex)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage());
        }
    }
}
