package com.spring.kodo.unittest;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.Tag;
import com.spring.kodo.entity.Transaction;
import com.spring.kodo.repository.TransactionRepository;
import com.spring.kodo.service.impl.AccountServiceImpl;
import com.spring.kodo.service.impl.CourseServiceImpl;
import com.spring.kodo.service.impl.TagServiceImpl;
import com.spring.kodo.service.impl.TransactionServiceImpl;
import com.spring.kodo.util.exception.AccountPermissionDeniedException;
import com.spring.kodo.util.exception.InputDataValidationException;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class TransactionServiceImplUnitTest
{
    @InjectMocks
    private TransactionServiceImpl transactionServiceImpl;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountServiceImpl accountServiceImpl;

    @Mock
    private CourseServiceImpl courseServiceImpl;

    @Mock
    private TagServiceImpl tagServiceImpl;

    @Test(expected = InputDataValidationException.class)
    public void whenCreateNewTransactionWithEmptyTransaction_thenInputDataValidationException() throws Exception
    {
        // PREPARATION
        Transaction unsavedTransaction = new Transaction();

        // ACTION
        transactionServiceImpl.createNewTransaction(unsavedTransaction, 1L, 1L, 1L);
    }

    @Test(expected = AccountPermissionDeniedException.class)
    public void whenGetLifeTimePlatformEarningsWithNonAdmin_thenAccountPermissionDeniedException() throws Exception
    {
        // PREPARATION
        Account savedAccount = new Account("test account", "testPassword1", "test name", "test bio", "test email", false);

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedAccount);

        // ACTION
        transactionServiceImpl.getLifetimePlatformEarnings(ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetLifeTimePlatformEarnings_thenReturnEarnings() throws Exception
    {
        // PREPARATION
        Account savedAccount = new Account("test account", "testPassword1", "test name", "test bio", "test email", true);
        BigDecimal expectedEarnings = new BigDecimal(10);

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedAccount);
        Mockito.when(transactionRepository.getLifetimePlatformEarning()).thenReturn(Optional.of(expectedEarnings));

        // ACTION
        BigDecimal actualEarnings = transactionServiceImpl.getLifetimePlatformEarnings(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(expectedEarnings.longValue(), actualEarnings.longValue());
    }

    @Test(expected = AccountPermissionDeniedException.class)
    public void whenGetCurrentMonthPlatformEarningsWithNonAdmin_thenAccountPermissionDeniedException() throws Exception
    {
        // PREPARATION
        Account savedAccount = new Account("test account", "testPassword1", "test name", "test bio", "test email", false);

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedAccount);

        // ACTION
        transactionServiceImpl.getCurrentMonthPlatformEarnings(ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetCurrentMonthPlatformEarnings_thenReturnEarnings() throws Exception
    {
        // PREPARATION
        Account savedAccount = new Account("test account", "testPassword1", "test name", "test bio", "test email", true);
        BigDecimal expectedEarnings = new BigDecimal(10);

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedAccount);
        Mockito.when(transactionRepository.getCurrentMonthPlatformEarning()).thenReturn(Optional.of(expectedEarnings));

        // ACTION
        BigDecimal actualEarnings = transactionServiceImpl.getCurrentMonthPlatformEarnings(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(expectedEarnings.longValue(), actualEarnings.longValue());
    }

    @Test(expected = AccountPermissionDeniedException.class)
    public void whenGetLastMonthPlatformEarningsWithNonAdmin_thenAccountPermissionDeniedException() throws Exception
    {
        // PREPARATION
        Account savedAccount = new Account("test account", "testPassword1", "test name", "test bio", "test email", false);

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedAccount);

        // ACTION
        transactionServiceImpl.getLastMonthPlatformEarnings(ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetLastMonthPlatformEarnings_thenReturnEarnings() throws Exception
    {
        // PREPARATION
        Account savedAccount = new Account("test account", "testPassword1", "test name", "test bio", "test email", true);
        BigDecimal expectedEarnings = new BigDecimal(10);

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedAccount);
        Mockito.when(transactionRepository.getLastMonthPlatformEarning()).thenReturn(Optional.of(expectedEarnings));

        // ACTION
        BigDecimal actualEarnings = transactionServiceImpl.getLastMonthPlatformEarnings(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(expectedEarnings.longValue(), actualEarnings.longValue());
    }

    @Test(expected = AccountPermissionDeniedException.class)
    public void whenGetLifetimeCourseEarningWithNonAdmin_thenAccountPermissionDeniedException() throws Exception
    {
        // PREPARATION
        Account savedAccount = new Account(1L, "test account", "testPassword1", "test name", "test bio", "test email", "test url",false);
        Course savedCourse = new Course(1L, "test course", "test description", new BigDecimal(10), "test url");

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedAccount);
        Mockito.when(courseServiceImpl.getCourseByCourseId(ArgumentMatchers.anyLong())).thenReturn(savedCourse);

        // ACTION
        transactionServiceImpl.getLifetimeCourseEarning(savedAccount.getAccountId(), savedCourse.getCourseId());
    }

    @Test
    public void whenGetLifetimeCourseEarning_thenReturnEarnings() throws Exception
    {
        // PREPARATION
        Account savedAccount = new Account(1L, "test account", "testPassword1", "test name", "test bio", "test email", "test url",true);
        Course savedCourse = new Course(1L, "test course", "test description", new BigDecimal(10), "test url");
        BigDecimal expectedEarnings = new BigDecimal(10);

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedAccount);
        Mockito.when(courseServiceImpl.getCourseByCourseId(ArgumentMatchers.anyLong())).thenReturn(savedCourse);
        Mockito.when(transactionRepository.getLifetimeCourseEarning(ArgumentMatchers.anyLong())).thenReturn(Optional.of(expectedEarnings));

        // ACTION
        BigDecimal actualEarnings = transactionServiceImpl.getLifetimeCourseEarning(savedAccount.getAccountId(), savedCourse.getCourseId());

        // ASSERTION
        assertEquals(expectedEarnings.longValue(), actualEarnings.longValue());
    }

    @Test(expected = AccountPermissionDeniedException.class)
    public void whenGetCurrentMonthCourseEarningWithNonAdmin_thenAccountPermissionDeniedException() throws Exception
    {
        // PREPARATION
        Account savedAccount = new Account(1L, "test account", "testPassword1", "test name", "test bio", "test email", "test url",false);
        Course savedCourse = new Course(1L, "test course", "test description", new BigDecimal(10), "test url");

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedAccount);
        Mockito.when(courseServiceImpl.getCourseByCourseId(ArgumentMatchers.anyLong())).thenReturn(savedCourse);

        // ACTION
        transactionServiceImpl.getCurrentMonthCourseEarning(savedAccount.getAccountId(), savedCourse.getCourseId());
    }

    @Test
    public void whenGetCurrentMonthCourseEarning_thenReturnEarnings() throws Exception
    {
        // PREPARATION
        Account savedAccount = new Account(1L, "test account", "testPassword1", "test name", "test bio", "test email", "test url",true);
        Course savedCourse = new Course(1L, "test course", "test description", new BigDecimal(10), "test url");
        BigDecimal expectedEarnings = new BigDecimal(10);

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedAccount);
        Mockito.when(courseServiceImpl.getCourseByCourseId(ArgumentMatchers.anyLong())).thenReturn(savedCourse);
        Mockito.when(transactionRepository.getCurrentMonthCourseEarning(ArgumentMatchers.anyLong())).thenReturn(Optional.of(expectedEarnings));

        // ACTION
        BigDecimal actualEarnings = transactionServiceImpl.getCurrentMonthCourseEarning(savedAccount.getAccountId(), savedCourse.getCourseId());

        // ASSERTION
        assertEquals(expectedEarnings.longValue(), actualEarnings.longValue());
    }

    @Test(expected = AccountPermissionDeniedException.class)
    public void whenGetLifetimeTagEarningWithNonAdmin_thenAccountPermissionDeniedException() throws Exception
    {
        // PREPARATION
        Account savedAccount = new Account(1L, "test account", "testPassword1", "test name", "test bio", "test email", "test url",false);
        Tag savedTag = new Tag(1L, "new tag");

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedAccount);
        Mockito.when(tagServiceImpl.getTagByTagId(ArgumentMatchers.anyLong())).thenReturn(savedTag);

        // ACTION
        transactionServiceImpl.getLifetimeTagEarning(savedAccount.getAccountId(), savedTag.getTagId());
    }

    @Test
    public void whenGetLifetimeTagEarning_thenReturnEarnings() throws Exception
    {
        // PREPARATION
        Account savedAccount = new Account(1L, "test account", "testPassword1", "test name", "test bio", "test email", "test url",true);
        Tag savedTag = new Tag(1L, "new tag");
        BigDecimal expectedEarnings = new BigDecimal(10);

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedAccount);
        Mockito.when(tagServiceImpl.getTagByTagId(ArgumentMatchers.anyLong())).thenReturn(savedTag);
        Mockito.when(transactionRepository.getLifetimeTagEarning(ArgumentMatchers.anyLong())).thenReturn(Optional.of(expectedEarnings));

        // ACTION
        BigDecimal actualEarnings = transactionServiceImpl.getLifetimeTagEarning(savedAccount.getAccountId(), savedTag.getTagId());

        // ASSERTION
        assertEquals(expectedEarnings.longValue(), actualEarnings.longValue());
    }

    @Test(expected = AccountPermissionDeniedException.class)
    public void whenGetCurrentMonthTagEarningWithNonAdmin_thenAccountPermissionDeniedException() throws Exception
    {
        // PREPARATION
        Account savedAccount = new Account(1L, "test account", "testPassword1", "test name", "test bio", "test email", "test url",false);
        Tag savedTag = new Tag(1L, "new tag");

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedAccount);
        Mockito.when(tagServiceImpl.getTagByTagId(ArgumentMatchers.anyLong())).thenReturn(savedTag);

        // ACTION
        transactionServiceImpl.getCurrentMonthTagEarning(savedAccount.getAccountId(), savedTag.getTagId());
    }

    @Test
    public void whenGetCurrentMonthTagEarning_thenReturnEarnings() throws Exception
    {
        // PREPARATION
        Account savedAccount = new Account(1L, "test account", "testPassword1", "test name", "test bio", "test email", "test url",true);
        Tag savedTag = new Tag(1L, "new tag");
        BigDecimal expectedEarnings = new BigDecimal(10);

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedAccount);
        Mockito.when(tagServiceImpl.getTagByTagId(ArgumentMatchers.anyLong())).thenReturn(savedTag);
        Mockito.when(transactionRepository.getCurrentMonthTagEarning(ArgumentMatchers.anyLong())).thenReturn(Optional.of(expectedEarnings));

        // ACTION
        BigDecimal actualEarnings = transactionServiceImpl.getCurrentMonthTagEarning(savedAccount.getAccountId(), savedTag.getTagId());

        // ASSERTION
        assertEquals(expectedEarnings.longValue(), actualEarnings.longValue());
    }

    @Test(expected = AccountPermissionDeniedException.class)
    public void whenGetLastMonthTagEarningWithNonAdmin_thenAccountPermissionDeniedException() throws Exception
    {
        // PREPARATION
        Account savedAccount = new Account(1L, "test account", "testPassword1", "test name", "test bio", "test email", "test url",false);
        Tag savedTag = new Tag(1L, "new tag");

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedAccount);
        Mockito.when(tagServiceImpl.getTagByTagId(ArgumentMatchers.anyLong())).thenReturn(savedTag);

        // ACTION
        transactionServiceImpl.getLastMonthTagEarning(savedAccount.getAccountId(), savedTag.getTagId());
    }

    @Test
    public void whenGetLastMonthTagEarning_thenReturnEarnings() throws Exception
    {
        // PREPARATION
        Account savedAccount = new Account(1L, "test account", "testPassword1", "test name", "test bio", "test email", "test url",true);
        Tag savedTag = new Tag(1L, "new tag");
        BigDecimal expectedEarnings = new BigDecimal(10);

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedAccount);
        Mockito.when(tagServiceImpl.getTagByTagId(ArgumentMatchers.anyLong())).thenReturn(savedTag);
        Mockito.when(transactionRepository.getLastMonthTagEarning(ArgumentMatchers.anyLong())).thenReturn(Optional.of(expectedEarnings));

        // ACTION
        BigDecimal actualEarnings = transactionServiceImpl.getLastMonthTagEarning(savedAccount.getAccountId(), savedTag.getTagId());

        // ASSERTION
        assertEquals(expectedEarnings.longValue(), actualEarnings.longValue());
    }

    @Test(expected = AccountPermissionDeniedException.class)
    public void whenGetLifetimeTutorEarningWithNonAdmin_thenAccountPermissionDeniedException() throws Exception
    {
        // PREPARATION
        Account savedRequestingAccount = new Account(1L, "test account", "testPassword1", "test name", "test bio", "test email", "test url",false);
        Account savedTutorAccount = new Account(2L, "test tutor account", "testPassword1", "test name", "test bio", "test email", "test url",false);

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedRequestingAccount).thenReturn(savedTutorAccount);

        // ACTION
        transactionServiceImpl.getLifetimeTutorEarning(savedRequestingAccount.getAccountId(), savedTutorAccount.getAccountId());
    }

    @Test
    public void whenGetLifetimeTutorEarning_thenReturnEarnings() throws Exception
    {
        // PREPARATION
        Account savedRequestingAccount = new Account(1L, "test account", "testPassword1", "test name", "test bio", "test email", "test url",true);
        Account savedTutorAccount = new Account(2L, "test tutor account", "testPassword1", "test name", "test bio", "test email", "test url",false);
        BigDecimal expectedEarnings = new BigDecimal(10);

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedRequestingAccount).thenReturn(savedTutorAccount);
        Mockito.when(transactionRepository.getLifetimeEarningsByPayeeId(ArgumentMatchers.anyLong())).thenReturn(Optional.of(expectedEarnings));

        // ACTION
        BigDecimal actualEarnings = transactionServiceImpl.getLifetimeTutorEarning(savedRequestingAccount.getAccountId(), savedTutorAccount.getAccountId());

        // ASSERTION
        assertEquals(expectedEarnings.longValue(), actualEarnings.longValue());
    }

    @Test(expected = AccountPermissionDeniedException.class)
    public void whenGetCurrentMonthTutorEarningWithNonAdmin_thenAccountPermissionDeniedException() throws Exception
    {
        // PREPARATION
        Account savedRequestingAccount = new Account(1L, "test account", "testPassword1", "test name", "test bio", "test email", "test url",false);
        Account savedTutorAccount = new Account(2L, "test tutor account", "testPassword1", "test name", "test bio", "test email", "test url",false);

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedRequestingAccount).thenReturn(savedTutorAccount);

        // ACTION
        transactionServiceImpl.getCurrentMonthTutorEarning(savedRequestingAccount.getAccountId(), savedTutorAccount.getAccountId());
    }

    @Test
    public void whenGetCurrentMonthTutorEarning_thenReturnEarnings() throws Exception
    {
        // PREPARATION
        Account savedRequestingAccount = new Account(1L, "test account", "testPassword1", "test name", "test bio", "test email", "test url",true);
        Account savedTutorAccount = new Account(2L, "test tutor account", "testPassword1", "test name", "test bio", "test email", "test url",false);
        BigDecimal expectedEarnings = new BigDecimal(10);

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedRequestingAccount).thenReturn(savedTutorAccount);
        Mockito.when(transactionRepository.getCurrentMonthEarningsByPayeeId(ArgumentMatchers.anyLong())).thenReturn(Optional.of(expectedEarnings));

        // ACTION
        BigDecimal actualEarnings = transactionServiceImpl.getCurrentMonthTutorEarning(savedRequestingAccount.getAccountId(), savedTutorAccount.getAccountId());

        // ASSERTION
        assertEquals(expectedEarnings.longValue(), actualEarnings.longValue());
    }

    @Test(expected = AccountPermissionDeniedException.class)
    public void whenGetLifetimeHighestEarningCoursesWithNonAdmin_thenAccountPermissionDeniedException() throws Exception
    {
        // PREPARATION
        Account savedAccount = new Account(1L, "test account", "testPassword1", "test name", "test bio", "test email", "test url",false);

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedAccount);

        // ACTION
        transactionServiceImpl.getLifetimeHighestEarningCourses(savedAccount.getAccountId());
    }

    @Test(expected = AccountPermissionDeniedException.class)
    public void whenGetLastMonthCourseEarningWithNonAdmin_thenAccountPermissionDeniedException() throws Exception
    {
        // PREPARATION
        Account savedAccount = new Account(1L, "test account", "testPassword1", "test name", "test bio", "test email", "test url",false);
        Course savedCourse = new Course(1L, "test course", "test description", new BigDecimal(10), "test url");

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedAccount);
        Mockito.when(courseServiceImpl.getCourseByCourseId(ArgumentMatchers.anyLong())).thenReturn(savedCourse);

        // ACTION
        transactionServiceImpl.getLastMonthCourseEarning(savedAccount.getAccountId(), savedCourse.getCourseId());
    }

    @Test
    public void whenGetLastMonthCourseEarning_thenReturnEarnings() throws Exception
    {
        // PREPARATION
        Account savedAccount = new Account(1L, "test account", "testPassword1", "test name", "test bio", "test email", "test url",true);
        Course savedCourse = new Course(1L, "test course", "test description", new BigDecimal(10), "test url");
        BigDecimal expectedEarnings = new BigDecimal(10);

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedAccount);
        Mockito.when(courseServiceImpl.getCourseByCourseId(ArgumentMatchers.anyLong())).thenReturn(savedCourse);
        Mockito.when(transactionRepository.getLastMonthCourseEarning(ArgumentMatchers.anyLong())).thenReturn(Optional.of(expectedEarnings));

        // ACTION
        BigDecimal actualEarnings = transactionServiceImpl.getLastMonthCourseEarning(savedAccount.getAccountId(), savedCourse.getCourseId());

        // ASSERTION
        assertEquals(expectedEarnings.longValue(), actualEarnings.longValue());
    }

    @Test(expected = AccountPermissionDeniedException.class)
    public void whenGetLastMonthTutorEarningsgWithNonAdmin_thenAccountPermissionDeniedException() throws Exception
    {
        // PREPARATION
        Account savedRequestingAccount = new Account(1L, "test account", "testPassword1", "test name", "test bio", "test email", "test url",false);
        Account savedTutorAccount = new Account(2L, "test tutor account", "testPassword1", "test name", "test bio", "test email", "test url",false);

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedRequestingAccount).thenReturn(savedTutorAccount);

        // ACTION
        transactionServiceImpl.getLastMonthTutorEarnings(savedRequestingAccount.getAccountId(), savedTutorAccount.getAccountId());
    }

    @Test
    public void whenGetLastMonthTutorEarnings_thenReturnEarnings() throws Exception
    {
        // PREPARATION
        Account savedRequestingAccount = new Account(1L, "test account", "testPassword1", "test name", "test bio", "test email", "test url",true);
        Account savedTutorAccount = new Account(2L, "test tutor account", "testPassword1", "test name", "test bio", "test email", "test url",false);
        BigDecimal expectedEarnings = new BigDecimal(10);

        Mockito.when(accountServiceImpl.getAccountByAccountId(ArgumentMatchers.anyLong())).thenReturn(savedRequestingAccount).thenReturn(savedTutorAccount);
        Mockito.when(transactionRepository.lastMonthTutorEarnings(ArgumentMatchers.anyLong())).thenReturn(Optional.of(expectedEarnings));

        // ACTION
        BigDecimal actualEarnings = transactionServiceImpl.getLastMonthTutorEarnings(savedRequestingAccount.getAccountId(), savedTutorAccount.getAccountId());

        // ASSERTION
        assertEquals(expectedEarnings.longValue(), actualEarnings.longValue());
    }
}
