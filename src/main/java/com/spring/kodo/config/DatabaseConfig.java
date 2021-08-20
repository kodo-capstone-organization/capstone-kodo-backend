package com.spring.kodo.config;

import com.spring.kodo.entity.Account;
import com.spring.kodo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class DatabaseConfig
{
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private Environment env;

    @EventListener(ApplicationReadyEvent.class)
    public void loadDataOnStartup() {
        System.out.println("\n===== Application started on port: " + env.getProperty("local.server.port") + " =====");
        System.out.println("\n===== 1. Loading Init Data to Database =====");

        // Populate Dummy Accounts
        List<Account> accounts = new ArrayList<>();
        for (int i = 1; i <= 10; i++)
        {
            Account account = new Account("account" + i, "password", "account" + i, "account" + i, "account" + i + "@gmail.com", "", false);
            accounts.add(account);
        }
        List<Account> savedAccounts = accountRepository.saveAll(accounts);
        List<Long> accountIds = savedAccounts.stream().map(Account::getAccountId).collect(Collectors.toList());
        System.out.println(">> Added Accounts with accountIds: " + accountIds);

        // Populate other dummy entities below
    }
}
