package com.spring.kodo.config;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Tag;
import com.spring.kodo.util.exception.*;
import com.spring.kodo.service.AccountService;
import com.spring.kodo.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class DatabaseConfig
{
    @Autowired
    private TagService tagService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private Environment env;

    @EventListener(ApplicationReadyEvent.class)
    public void loadDataOnStartup()
    {
        System.out.println("\n===== Application started on port: " + env.getProperty("local.server.port") + " =====");
        System.out.println("\n===== 1. Loading Init Data to Database =====");

        // Lists definition
        List<Account> accounts = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();

        // Populate data lists
        addAccounts(accounts);
        addTags(tags);

        // Save data lists to Database
        accountService.createNewAccounts(accounts);
        tagService.createNewTags(tags);

        // Test add Tag to Account
        try
        {
            for (int i = 1; i < accounts.size(); i++)
            {
                for (int j = 0; j < tags.size(); j++)
                {
                    accountService.addTagToAccount(accounts.get(i), tags.get(j));
                }
            }
        }
        catch (AccountNotFoundException | TagNotFoundException | UpdateAccountException ex)
        {
            System.err.println(ex.getMessage());
        }

        // Print Ids of saved data list
        List<Long> accountIds = accounts.stream().map(Account::getAccountId).collect(Collectors.toList());
        System.out.println(">> Added Accounts with accountIds: " + accountIds);

        List<Long> tagIds = tags.stream().map(Tag::getTagId).collect(Collectors.toList());
        System.out.println(">> Added Tags with tagIds: " + tagIds);

        System.out.println("===== Init Data Fully Loaded to Database =====");

        // Tutor 2 tests
        System.out.println("\n===== 2. Tutor 2 Tests =====");
        testTutor2();
        System.out.println("\n===== Tutor 2 Tests Completed =====");
    }

    private void testTutor2()
    {
        // Test Creating new account with a list of tag titles
        System.out.println(">> Simulating frontend account creation - passing in account details and a list of tagTitles");
        Account newAccount = new Account("tutor2", "password", "Tutor 2", "I am Tutor 2", "tutor2@gmail.com", "https://tutor2URL.com", false);
        List<String> tagTitles = Arrays.asList("Bash", "BASIC", "F", "F#", "F*");

        try
        {
            Account account = accountService.createNewAccount(newAccount, tagTitles);
            System.out.println("    | Account with ID: " + account.getAccountId() + " successfully created with tags: " + account.getInterests());
            System.out.println("    | Check that tag created has new ID while the other references a previously-created Tag ID");
        }
        catch
        (InputDataValidationException ex)
        {
            System.err.println(ex.getMessage());
        }

        // Test Login Success
        try
        {
            Account account = accountService.login("tutor2", "password");
            System.out.println("    | Account with ID: " + account.getAccountId() + " successfully login: " + account.getName());
        }
        catch(InvalidLoginCredentialsException | AccountNotFoundException ex)
        {
            System.err.println(ex.getMessage());
        }

        // Test Login Failure
        try
        {
            Account account = accountService.login("tutor2", "password123");
            System.out.println("    | Account with ID: " + account.getAccountId() + " successfully login: " + account.getName());
        }
        catch(InvalidLoginCredentialsException | AccountNotFoundException ex)
        {
            System.out.println(ex.getMessage()); // using out instead of err just because
        }
    }

    private void addAccounts(List<Account> accounts)
    {
        accounts.add(new Account("admin", "password", "Admin", "I am Admin", "admin@gmail.com", "https://adminURL.com", true));
        accounts.add(new Account("student1", "password", "Student 1", "I am Student 1", "student1@gmail.com", "https://student1URL.com", false));
        accounts.add(new Account("student2", "password", "Student 2", "I am Student 2", "student2@gmail.com", "https://student2URL.com", false));
        accounts.add(new Account("tutor1", "password", "Tutor 1", "I am Tutor 1", "tutor1@gmail.com", "https://tutor1URL.com", false));
    }

    private void addTags(List<Tag> tags)
    {
        tags.add(new Tag("Python"));
        tags.add(new Tag("JavaScript"));
        tags.add(new Tag("Java"));
        tags.add(new Tag("C#"));
        tags.add(new Tag("C"));
        tags.add(new Tag("C++"));
        tags.add(new Tag("Go"));
        tags.add(new Tag("R"));
        tags.add(new Tag("Swift"));
    }
}

