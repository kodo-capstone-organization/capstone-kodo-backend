package com.spring.kodo.account;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AccountConfig
{
    @Bean
    CommandLineRunner commandLineRunner(AccountRepository accountRepository)
    {
        return args ->
        {
            List<Account> accounts = new ArrayList<>();

            for (int i = 1; i <= 5; i++)
            {
                Account account = new Account("account" + i, "password", "account" + i, "account" + i, "account" + i + "@gmail.com", "", false);
                accounts.add(account);
            }

            accountRepository.saveAll(accounts);
        };
    }
}
