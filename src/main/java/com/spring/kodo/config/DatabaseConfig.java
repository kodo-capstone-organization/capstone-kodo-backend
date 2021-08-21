package com.spring.kodo.config;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Tag;
import com.spring.kodo.repository.AccountRepository;
import com.spring.kodo.repository.TagRepository;
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
    private TagRepository tagRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private Environment env;

    @EventListener(ApplicationReadyEvent.class)
    public void loadDataOnStartup()
    {
        System.out.println("\n===== Application started on port: " + env.getProperty("local.server.port") + " =====");
        System.out.println("\n===== 1. Loading Init Data to Database =====");

        // Populate Dummy Tags
        List<Tag> savedTags = tagRepository.saveAll(
                Arrays.asList(
                        new Tag("Java"),
                        new Tag("Go"),
                        new Tag("Python"),
                        new Tag("C"),
                        new Tag("C#")
                )
        );
        List<Long> tagIds = savedTags.stream().map(Tag::getTagId).collect(Collectors.toList());
        System.out.println(">> Added Tags with tagIds: " + tagIds);

        // Populate Dummy Accounts
        // Test add Tag to Account

        List<Account> savedAccounts = Arrays.asList(
                new Account("admin", "password", "Admin", "I am Admin", "admin@gmail.com", "https://adminURL.com", true),
                new Account("student1", "password", "Student 1", "I am Student 1", "student1@gmail.com", "https://student1URL.com", false),
                new Account("student2", "password", "Student 2", "I am Student 2", "student2@gmail.com", "https://student2URL.com", false),
                new Account("tutor1", "password", "Tutor 1", "I am Tutor 1", "tutor1@gmail.com", "https://tutor1URL.com", false)
        );

        savedAccounts.get(1).getInterests().addAll(savedTags);
        savedAccounts = accountRepository.saveAll(savedAccounts);

//        List<Account> savedAccounts = accountRepository.saveAll(
//                Arrays.asList(
//                        new Account("admin", "password", "Admin", "I am Admin", "admin@gmail.com", "https://adminURL.com", true),
//                        new Account("student1", "password", "Student 1", "I am Student 1", "student1@gmail.com", "https://student1URL.com", false),
//                        new Account("student2", "password", "Student 2", "I am Student 2", "student2@gmail.com", "https://student2URL.com", false),
//                        new Account("tutor1", "password", "Tutor 1", "I am Tutor 1", "tutor1@gmail.com", "https://tutor1URL.com", false)
//                )
//        );

        List<Long> accountIds = savedAccounts.stream().map(Account::getAccountId).collect(Collectors.toList());
        System.out.println(">> Added Accounts with accountIds: " + accountIds);

        // Populate other dummy entities below
    }
}
