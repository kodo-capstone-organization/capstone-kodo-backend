package com.spring.kodo.integrationtest;

import com.spring.kodo.entity.Account;
import com.spring.kodo.repository.AccountRepository;
import com.spring.kodo.service.impl.AccountServiceImpl;
import com.spring.kodo.util.exception.AccountUsernameExistException;
import com.spring.kodo.util.exception.InputDataValidationException;
import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplIntegrationTest
{
    @InjectMocks
    private AccountServiceImpl accountServiceImpl;

    @Mock
    private AccountRepository accountRepository;

//    @Before
//    public void setUp() throws Exception
//    {
//    }

    @Test
    public void whenCreateNewAccount_thenReturnAccount() throws Exception
    {
        // PREPARATION
        Account savedAccount = new Account(1L, "test1", "Test1Test1Test1", "test1", "test1", "test1@email.com", null, false);
        Account unsavedAccount = new Account("test1", "Test1Test1Test1", "test1", "test1", "test1@email.com", null, false);

        // ACTION
        Account retrievedAccount = accountServiceImpl.createNewAccount(unsavedAccount);

        // ASSERTION
        assertNotNull(retrievedAccount.getAccountId());
        assertEquals(retrievedAccount.getName(), savedAccount.getName());
    }

    @Test(expected = InputDataValidationException.class)
    public void whenCreateNewAccount_thenInputDataValidationException() throws Exception
    {
        // PREPARATION
        Account unsavedAccount = new Account("test1", "Test1Test1Test1", "test1", "test1", "test1", null, false);

        // ACTION
        accountServiceImpl.createNewAccount(unsavedAccount);
    }

    @Test
    public void whenGetAccount_thenReturnAccount() throws Exception
    {
        // PREPARATION
        Account savedAccount = Mockito.mock(Account.class);

        Mockito.when(accountRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedAccount));

        // ACTION
        Account retrievedAccount = accountServiceImpl.getAccountByAccountId(1L);

        // ASSERTION
        assertEquals(retrievedAccount.getName(), savedAccount.getName());
    }

    @Test
    public void whenGetAllAccounts_thenReturnAllAccounts() throws Exception
    {
        // PREPARATION
        int accountsSize = 3;

        List<Account> savedAccounts = new ArrayList<>();
        for (int i = 0; i < accountsSize; i++)
        {
            savedAccounts.add(Mockito.mock(Account.class));
        }

        Mockito.when(accountRepository.findAll()).thenReturn(savedAccounts);

        // ACTION
        List<Account> retrievedAccounts = accountServiceImpl.getAllAccounts();

        // ASSERTION
        for (int i = 0; i < accountsSize; i++)
        {
            assertNotNull(savedAccounts.get(i).getAccountId());
            assertNotNull(retrievedAccounts.get(i).getAccountId());
            assertEquals(savedAccounts.get(i).getAccountId().longValue(), retrievedAccounts.get(i).getAccountId().longValue());
        }
    }
}
