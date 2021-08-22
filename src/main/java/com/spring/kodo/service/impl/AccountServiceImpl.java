package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Tag;
import com.spring.kodo.exception.AccountNotFoundException;
import com.spring.kodo.exception.TagNotFoundException;
import com.spring.kodo.repository.AccountRepository;
import com.spring.kodo.service.AccountService;
import com.spring.kodo.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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
    public Optional<Account> getAccountByAccountId(Long accountId)
    {
        return accountRepository.findById(accountId);
    }

    @Override
    public Optional<Account> getAccountByUsername(String username)
    {
        return accountRepository.findByUsername(username);
    }

    @Override
    public Optional<Account> getAccountByName(String name)
    {
        return accountRepository.findByName(name);
    }

    @Override
    public Optional<Account> getAccountByEmail(String email)
    {
        return accountRepository.findByEmail(email);
    }

    @Override
    public List<Account> getAccounts()
    {
        return accountRepository.findAll();
    }

    @Override
    public Account addTagToAccount(Account account, Tag tag)
    {
        account = getAccountByAccountId(account.getAccountId()).orElseThrow(AccountNotFoundException::new);;
        tag = tagService.getTagByTagId(tag.getTagId()).orElseThrow(TagNotFoundException::new);

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
}

