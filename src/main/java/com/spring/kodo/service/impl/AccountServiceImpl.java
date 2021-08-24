package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Tag;
import com.spring.kodo.util.MessageFormatterUtil;
import com.spring.kodo.util.cryptography.CryptographicHelper;
import com.spring.kodo.util.exception.*;
import com.spring.kodo.repository.AccountRepository;
import com.spring.kodo.service.AccountService;
import com.spring.kodo.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class AccountServiceImpl implements AccountService
{
    @Autowired // With this annotation, we do not to populate AccountRepository in this class' constructor
    private AccountRepository accountRepository;
    @Autowired
    private TagService tagService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public AccountServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Account createNewAccount(Account newAccount, List<String> tagTitles) throws InputDataValidationException
    {
        Set<ConstraintViolation<Account>> constraintViolations = validator.validate(newAccount);
        if(constraintViolations.isEmpty())
        {
            // Persist Account
            Account persistedAccount = accountRepository.saveAndFlush(newAccount);

            // Process Tags
            if(tagTitles != null && (!tagTitles.isEmpty()))
            {
                for(String tagTitle: tagTitles)
                {
                    Tag tag = tagService.getTagByTitleOrCreateNew(tagTitle);
                    try
                    {
                        persistedAccount = addTagToAccount(persistedAccount, tag);
                    }
                    catch (TagNotFoundException | AccountNotFoundException | UpdateAccountException ex)
                    {
                        // Since we are still in a creation step, these generic exceptions will probably not happen / can kinda be ignored
                        // e.g. acc not found, duplicate tag exception
                        continue;
                    }
                }
            }

            accountRepository.saveAndFlush(persistedAccount);
            return persistedAccount;
        }
        else
        {
            throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
        }
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
        return accountRepository.findAll();
    }

    @Override
    public Account addTagToAccount(Account account, Tag tag) throws AccountNotFoundException, TagNotFoundException, UpdateAccountException {
        account = getAccountByAccountId(account.getAccountId());
        tag = tagService.getTagByTagId(tag.getTagId());

        if (!account.getInterests().contains(tag))
        {
            account.getInterests().add(tag);
        }
        else
        {
            throw new UpdateAccountException("Unable to add tag with title: " + tag.getTitle() +
                    " to account with ID: " + account.getAccountId() + " as tag is already linked to this account");
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

    public Account login(String username, String password) throws InvalidLoginCredentialsException, AccountNotFoundException
    {
        if (username != null)
        {
            Account account = getAccountByUsername(username);
            String hashedPassword = CryptographicHelper.getSHA256Digest(password, account.getSalt());

            if (account.getPassword().equals(hashedPassword))
            {
                return account;
            }
            else
            {
                throw new InvalidLoginCredentialsException("Username or Password is invalid");
            }
        }
        else
        {
            throw new InvalidLoginCredentialsException("Username or Password is invalid");
        }
    }
}

