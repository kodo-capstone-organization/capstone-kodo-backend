package com.spring.kodo.unittest;

import com.spring.kodo.entity.Account;
import com.spring.kodo.repository.AccountRepository;
import com.spring.kodo.service.impl.AccountServiceImpl;
import com.spring.kodo.util.exception.AccountNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.InvalidLoginCredentialsException;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class AccountServiceImplUnitTest
{
    @InjectMocks
    private AccountServiceImpl accountServiceImpl;

    @Mock
    private AccountRepository accountRepository;


    @Test
    public void whenCreateNewAccount_thenReturnAccount() throws Exception
    {
        // PREPARATION
        Account savedAccount = new Account(1L, "test1", "Test1Test1Test1", "test1", "test1", "test1@email.com", null, false);
        Account unsavedAccount = new Account("test1", "Test1Test1Test1", "test1", "test1", "test1@email.com", null, false);

        Mockito.when(accountRepository.saveAndFlush(ArgumentMatchers.any(Account.class))).thenReturn(savedAccount);

        // ACTION
        Account retrievedAccount = accountServiceImpl.createNewAccount(unsavedAccount);

        // ASSERTION
        assertEquals(savedAccount.getName(), retrievedAccount.getName());
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
    public void whenGetAccountByAccountId_thenReturnAccount() throws Exception
    {
        // PREPARATION
        Account savedAccount = Mockito.mock(Account.class);

        Mockito.when(accountRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedAccount));

        // ACTION
        Account retrievedAccount = accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedAccount.getName(), retrievedAccount.getName());
    }

    @Test(expected = AccountNotFoundException.class)
    public void whenGetAccountByAccountId_thenAccountNotFoundException() throws Exception
    {
        // ACTION
        Account retrievedAccount = accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetAccountByUsername_thenReturnAccount() throws Exception
    {
        // PREPARATION
        Account savedAccount = Mockito.mock(Account.class);

        Mockito.when(accountRepository.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(savedAccount));

        // ACTION
        Account retrievedAccount = accountServiceImpl.getAccountByUsername(ArgumentMatchers.anyString());

        // ASSERTION
        assertEquals(savedAccount.getName(), retrievedAccount.getName());
    }

    @Test(expected = AccountNotFoundException.class)
    public void whenGetAccountByUsername_thenAccountNotFoundException() throws Exception
    {
        // ACTION
        Account retrievedAccount = accountServiceImpl.getAccountByUsername("test1");
    }

    @Test
    public void whenGetAccountByEmail_thenReturnAccount() throws Exception
    {
        // PREPARATION
        Account savedAccount = Mockito.mock(Account.class);

        Mockito.when(accountRepository.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(savedAccount));

        // ACTION
        Account retrievedAccount = accountServiceImpl.getAccountByEmail(ArgumentMatchers.anyString());

        // ASSERTION
        assertEquals(savedAccount.getName(), retrievedAccount.getName());
    }

    @Test(expected = AccountNotFoundException.class)
    public void whenGetAccountByEmail_thenAccountNotFoundException() throws Exception
    {
        // ACTION
        Account retrievedAccount = accountServiceImpl.getAccountByEmail("test1@test.com");
    }

    @Test
    public void whenGetAccountByCourseId_thenReturnAccount() throws Exception
    {
        // PREPARATION
        Account savedAccount = Mockito.mock(Account.class);

        Mockito.when(accountRepository.findByCourseId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedAccount));

        // ACTION
        Account retrievedAccount = accountServiceImpl.getAccountByCourseId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedAccount.getName(), retrievedAccount.getName());
    }

    @Test(expected = AccountNotFoundException.class)
    public void whenGetAccountByCourseId_thenAccountNotFoundException() throws Exception
    {
        // ACTION
        Account retrievedAccount = accountServiceImpl.getAccountByCourseId(ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetAccountByLessonId_thenReturnAccount() throws Exception
    {
        // PREPARATION
        Account savedAccount = Mockito.mock(Account.class);

        Mockito.when(accountRepository.findByLessonId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedAccount));

        // ACTION
        Account retrievedAccount = accountServiceImpl.getAccountByLessonId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedAccount.getName(), retrievedAccount.getName());
    }

    @Test(expected = AccountNotFoundException.class)
    public void whenGetAccountByLessonId_thenAccountNotFoundException() throws Exception
    {
        // ACTION
        Account retrievedAccount = accountServiceImpl.getAccountByLessonId(ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetAccountByEnrolledCourseId_thenReturnAccount() throws Exception
    {
        // PREPARATION
        Account savedAccount = Mockito.mock(Account.class);

        Mockito.when(accountRepository.findByEnrolledCourseId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedAccount));

        // ACTION
        Account retrievedAccount = accountServiceImpl.getAccountByEnrolledCourseId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedAccount.getName(), retrievedAccount.getName());
    }

    @Test(expected = AccountNotFoundException.class)
    public void whenGetAccountByEnrolledCourseId_thenAccountNotFoundException() throws Exception
    {
        // ACTION
        Account retrievedAccount = accountServiceImpl.getAccountByEnrolledCourseId(ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetAccountByEnrolledLessonId_thenReturnAccount() throws Exception
    {
        // PREPARATION
        Account savedAccount = Mockito.mock(Account.class);

        Mockito.when(accountRepository.findByEnrolledLessonId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedAccount));

        // ACTION
        Account retrievedAccount = accountServiceImpl.getAccountByEnrolledLessonId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedAccount.getName(), retrievedAccount.getName());
    }

    @Test(expected = AccountNotFoundException.class)
    public void whenGetAccountByEnrolledLessonId_thenAccountNotFoundException() throws Exception
    {
        // ACTION
        Account retrievedAccount = accountServiceImpl.getAccountByEnrolledLessonId(ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetAccountByEnrolledContentId_thenReturnAccount() throws Exception
    {
        // PREPARATION
        Account savedAccount = Mockito.mock(Account.class);

        Mockito.when(accountRepository.findByEnrolledContentId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedAccount));

        // ACTION
        Account retrievedAccount = accountServiceImpl.getAccountByEnrolledContentId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedAccount.getName(), retrievedAccount.getName());
    }

    @Test(expected = AccountNotFoundException.class)
    public void whenGetAccountByEnrolledContentId_thenAccountNotFoundException() throws Exception
    {
        // ACTION
        Account retrievedAccount = accountServiceImpl.getAccountByEnrolledContentId(ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetAccountByStudentAttemptId_thenReturnAccount() throws Exception
    {
        // PREPARATION
        Account savedAccount = Mockito.mock(Account.class);

        Mockito.when(accountRepository.findByStudentAttemptId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedAccount));

        // ACTION
        Account retrievedAccount = accountServiceImpl.getAccountByStudentAttemptId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedAccount.getName(), retrievedAccount.getName());
    }

    @Test(expected = AccountNotFoundException.class)
    public void whenGetAccountByStudentAttemptId_thenAccountNotFoundException() throws Exception
    {
        // ACTION
        Account retrievedAccount = accountServiceImpl.getAccountByStudentAttemptId(ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetAccountByQuizId_thenReturnAccount() throws Exception
    {
        // PREPARATION
        Account savedAccount = Mockito.mock(Account.class);

        Mockito.when(accountRepository.findByQuizId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedAccount));

        // ACTION
        Account retrievedAccount = accountServiceImpl.getAccountByQuizId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedAccount.getName(), retrievedAccount.getName());
    }

    @Test(expected = AccountNotFoundException.class)
    public void whenGetAccountByQuizId_thenAccountNotFoundException() throws Exception
    {
        // ACTION
        Account retrievedAccount = accountServiceImpl.getAccountByQuizId(ArgumentMatchers.anyLong());
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

        assertEquals(accountsSize, retrievedAccounts.size());

        // ASSERTION
        for (int i = 0; i < accountsSize; i++)
        {
            assertNotNull(savedAccounts.get(i).getAccountId());
            assertNotNull(retrievedAccounts.get(i).getAccountId());
            assertEquals(savedAccounts.get(i).getAccountId().longValue(), retrievedAccounts.get(i).getAccountId().longValue());
        }
    }

    @Test
    public void whenGetAllAccountsByTagId_thenReturnAllAccounts() throws Exception
    {
        // PREPARATION
        int accountsSize = 3;

        List<Account> savedAccounts = new ArrayList<>();
        for (int i = 0; i < accountsSize; i++)
        {
            savedAccounts.add(Mockito.mock(Account.class));
        }

        Mockito.when(accountRepository.findAllAccountsByTagId(ArgumentMatchers.anyLong())).thenReturn(savedAccounts);

        // ACTION
        List<Account> retrievedAccounts = accountServiceImpl.getAllAccountsByTagId(ArgumentMatchers.anyLong());

        assertEquals(accountsSize, retrievedAccounts.size());

        // ASSERTION
        for (int i = 0; i < accountsSize; i++)
        {
            assertNotNull(savedAccounts.get(i).getAccountId());
            assertNotNull(retrievedAccounts.get(i).getAccountId());
            assertEquals(savedAccounts.get(i).getAccountId().longValue(), retrievedAccounts.get(i).getAccountId().longValue());
        }
    }

    @Test
    public void whenUserLogin_thenReturnAccount() throws Exception
    {
        Account savedAccount = new Account(1L, "test1", "Test1Test1Test1", "test1", "test1", "test1@email.com", null, false);

        Mockito.when(accountRepository.findByUsername("test1"))
                .thenReturn(Optional.of(savedAccount));

        Account loggedInAccount = accountServiceImpl.userLogin("test1", "Test1Test1Test1");

        assertEquals(savedAccount.getName(), loggedInAccount.getName());
    }

    @Test(expected = InvalidLoginCredentialsException.class)
    public void whenUserLogin_thenInvalidLoginCredentialsException() throws Exception
    {
        Account loggedInAccount = accountServiceImpl.userLogin("test1", "Test1Test1Test1");
    }

    @Test
    public void whenAdminLogin_thenReturnAccount() throws Exception
    {
        Account savedAccount = new Account(1L, "test1", "Test1Test1Test1", "test1", "test1", "test1@email.com", null, true);

        Mockito.when(accountRepository.findByUsername("test1"))
                .thenReturn(Optional.of(savedAccount));

        Account loggedInAccount = accountServiceImpl.adminLogin("test1", "Test1Test1Test1");

        assertEquals(savedAccount.getName(), loggedInAccount.getName());
    }

    @Test(expected = InvalidLoginCredentialsException.class)
    public void whenAdminLogin_thenInvalidLoginCredentialsException() throws Exception
    {
        Account loggedInAccount = accountServiceImpl.adminLogin("test1", "Test1Test1Test1");
    }
}
