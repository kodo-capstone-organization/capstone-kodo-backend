package com.spring.kodo.service.inter;

import com.spring.kodo.entity.Transaction;
import com.spring.kodo.util.exception.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TransactionService
{
    Transaction createNewTransaction(Transaction newTransaction, Long payerId, Long payeeId, Long courseId) throws InputDataValidationException, TransactionStripeTransactionIdExistsException, UnknownPersistenceException, CourseNotFoundException, AccountNotFoundException;

    Boolean isAccountExistsByStripeTransactionId(String stripeTransactionId);

    List<Transaction> getAllPlatformTransactions(Long requestingAccountId) throws AccountNotFoundException, AccountPermissionDeniedException;

    List<Transaction> getAllPaymentsByAccountId(Long requestingAccountId) throws AccountNotFoundException;

    BigDecimal getLifetimeTotalEarningsByAccountId(Long requestingAccountId) throws AccountNotFoundException;

    List<Map<String, String>> getLifetimeEarningsByCourseByAccountId(Long requestingAccountId) throws AccountNotFoundException;

    BigDecimal getCurrentMonthTotalEarningsByAccountId(Long requestingAccountId) throws AccountNotFoundException;

    List<Map<String, String>> getCurrentMonthEarningsByCourseByAccountId(Long requestingAccountId) throws AccountNotFoundException;

    List<Map<String, String>> getCourseStatsByMonthForLastYear(Long requestingAccountId) throws AccountNotFoundException;

    BigDecimal getLifetimePlatformEarnings(Long requestingAccountId) throws AccountPermissionDeniedException, AccountNotFoundException;

    BigDecimal getCurrentMonthPlatformEarnings(Long requestingAccountId) throws AccountPermissionDeniedException, AccountNotFoundException;

    BigDecimal getLastMonthPlatformEarnings(Long requestingAccountId) throws AccountPermissionDeniedException, AccountNotFoundException;

    Map<String, BigDecimal> getMonthlyPlatformEarningsForLastYear(Long requestingAccountId) throws AccountPermissionDeniedException, AccountNotFoundException;

    public BigDecimal getLifetimeCourseEarning(Long requestingAccountId, Long courseId) throws AccountPermissionDeniedException, AccountNotFoundException, CourseNotFoundException;

    public BigDecimal getCurrentMonthCourseEarning(Long requestingAccountId, Long courseId) throws AccountNotFoundException, CourseNotFoundException, AccountPermissionDeniedException;

    public Map<String, BigDecimal> getMonthlyCourseEarningForLastYear(Long requestingAccountId, Long courseId) throws AccountPermissionDeniedException, AccountNotFoundException, CourseNotFoundException;

    public BigDecimal getLifetimeTagEarning(Long requestingAccountId, Long tagId) throws AccountPermissionDeniedException, AccountNotFoundException, TagNotFoundException;

    public BigDecimal getCurrentMonthTagEarning(Long requestingAccountId, Long tagId) throws AccountPermissionDeniedException, AccountNotFoundException, TagNotFoundException;

    public Map<String, BigDecimal> getMonthlyTagEarningForLastYear(Long requestingAccountId, Long tagId) throws AccountPermissionDeniedException, AccountNotFoundException, TagNotFoundException;

    public BigDecimal getLifetimeTutorEarning(Long requestingAccountId, Long tutorId) throws AccountPermissionDeniedException, AccountNotFoundException;

    public BigDecimal getCurrentMonthTutorEarning(Long requestingAccountId, Long tutorId) throws AccountPermissionDeniedException, AccountNotFoundException;

    public Map<String, BigDecimal> getMonthlyTutorEarningForLastYear(Long requestingAccountId, Long tutorId) throws AccountPermissionDeniedException, AccountNotFoundException;
}
