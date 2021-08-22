package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Tag;
import com.spring.kodo.util.exception.AccountNotFoundException;
import com.spring.kodo.util.exception.AccountPermissionDeniedException;
import com.spring.kodo.util.exception.TagNotFoundException;
import com.spring.kodo.repository.AccountRepository;
import com.spring.kodo.service.AccountService;
import com.spring.kodo.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService
{
    @Autowired // With this annotation, we do not to populate AccountRepository in this class' constructor
    private AccountRepository accountRepository;

    @Autowired
    private TagService tagService;

    @Override
    public Account createNewAccount(Account account)
    {
        return accountRepository.save(account);
    }

    @Override
    public List<Account> createNewAccounts(List<Account> accounts)
    {
        return accountRepository.saveAll(accounts);
    }

    @Override
    public Account getAccountByAccountId(Long accountId) throws AccountNotFoundException
    {
        Account account = accountRepository.findById(accountId).orElse(null);

        if (account != null)
        {
            return account;
        }
        else
        {
            throw new AccountNotFoundException("Account with ID: " + accountId + " does not exist!");
        }
    }

    @Override
    public Account getAccountByUsername(String username) throws AccountNotFoundException
    {
        Account account = accountRepository.findByUsername(username).orElse(null);

        if (account != null)
        {
            return account;
        }
        else
        {
            throw new AccountNotFoundException("Account with Username: " + username + " does not exist!");
        }
    }

    @Override
    public Account getAccountByEmail(String email) throws AccountNotFoundException
    {
        Account account = accountRepository.findByEmail(email).orElse(null);

        if (account != null)
        {
            return account;
        }
        else
        {
            throw new AccountNotFoundException("Account with Email: " + email + " does not exist!");
        }
    }

    @Override
    public List<Account> getAllAccounts()
    {
        return (List<Account>) accountRepository.findAll();
    }

    @Override
    public Account addTagToAccount(Account account, Tag tag) throws AccountNotFoundException, TagNotFoundException
    {
        account = getAccountByAccountId(account.getAccountId());
        tag = tagService.getTagByTagId(tag.getTagId());

        if (!account.getInterests().contains(tag))
        {
            account.getInterests().add(tag);
        }
        else
        {
            // throw duplicate add tag error
        }

        accountRepository.saveAndFlush(account);
        return account;
    }

    @Override
    public Long deactivateAccount(Long deactivatingAccountId, Long requestingAccountId) throws AccountNotFoundException, AccountPermissionDeniedException
    {
        if (deactivatingAccountId == requestingAccountId)
        {
            throw new AccountPermissionDeniedException("You cannot deactivate your own account");
        }

        Account requestingAccount = getAccountByAccountId(requestingAccountId);
        Account deactivatingAccount = getAccountByAccountId(deactivatingAccountId);

        // Check that requestingAccount is an admin account
        if (requestingAccount.getIsAdmin())
        {
            deactivatingAccount.setIsActive(Boolean.FALSE);
            Account deactivatedAccount = accountRepository.saveAndFlush(deactivatingAccount);
            return deactivatedAccount.getAccountId();
        }
        else
        {
            throw new AccountPermissionDeniedException("You do not have administrative rights to deactivate accounts.");
        }
    }
}

