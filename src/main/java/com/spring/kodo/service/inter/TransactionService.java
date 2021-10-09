package com.spring.kodo.service.inter;

import com.spring.kodo.entity.Transaction;
import com.spring.kodo.restentity.response.CourseWithEarningResp;
import com.spring.kodo.restentity.response.MonthlyEarningResp;
import com.spring.kodo.restentity.response.TransactionWithParticularsResp;
import com.spring.kodo.restentity.response.TutorWithEarningResp;
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

    // takes in tutor's account id
    List<Transaction> getAllEarningsByAccountId(Long requestingAccountId) throws AccountNotFoundException;

    BigDecimal getLifetimeTotalEarningsByAccountId(Long requestingAccountId) throws AccountNotFoundException;

    BigDecimal getLifetimeTutorPayoutByCourseId(Long courseId);

    List<Map<String, String>> getLifetimeEarningsByCourseByAccountId(Long requestingAccountId) throws AccountNotFoundException;

    BigDecimal getCurrentMonthTotalEarningsByAccountId(Long requestingAccountId) throws AccountNotFoundException;

    BigDecimal getAverageMonthlyCourseEarning(Long courseId);

    String getHighestEarningMonthWithValueByCourseId(Long courseId);

    List<Map<String, String>> getCurrentMonthEarningsByCourseByAccountId(Long requestingAccountId) throws AccountNotFoundException;

    List<Map<String, String>> getCourseStatsByMonthForLastYear(Long requestingAccountId) throws AccountNotFoundException, CourseNotFoundException, AccountPermissionDeniedException;

    BigDecimal getLifetimePlatformEarnings(Long requestingAccountId) throws AccountPermissionDeniedException, AccountNotFoundException;

    BigDecimal getCurrentMonthPlatformEarnings(Long requestingAccountId) throws AccountPermissionDeniedException, AccountNotFoundException;

    BigDecimal getLastMonthPlatformEarnings(Long requestingAccountId) throws AccountPermissionDeniedException, AccountNotFoundException;

    List<MonthlyEarningResp> getMonthlyPlatformEarningsForLastYear(Long requestingAccountId) throws AccountPermissionDeniedException, AccountNotFoundException;

    BigDecimal getLifetimeCourseEarning(Long requestingAccountId, Long courseId) throws AccountPermissionDeniedException, AccountNotFoundException, CourseNotFoundException;

    BigDecimal getCurrentMonthCourseEarning(Long requestingAccountId, Long courseId) throws AccountNotFoundException, CourseNotFoundException, AccountPermissionDeniedException;

    List<MonthlyEarningResp> getMonthlyCourseEarningForLastYear(Long requestingAccountId, Long courseId) throws AccountPermissionDeniedException, AccountNotFoundException, CourseNotFoundException;

    BigDecimal getLifetimeTagEarning(Long requestingAccountId, Long tagId) throws AccountPermissionDeniedException, AccountNotFoundException, TagNotFoundException;

    BigDecimal getCurrentMonthTagEarning(Long requestingAccountId, Long tagId) throws AccountPermissionDeniedException, AccountNotFoundException, TagNotFoundException;

    BigDecimal getLastMonthTagEarning(Long requestingAccountId, Long tagId) throws AccountPermissionDeniedException, AccountNotFoundException, TagNotFoundException;

    List<MonthlyEarningResp> getMonthlyTagEarningForLastYear(Long requestingAccountId, Long tagId) throws AccountPermissionDeniedException, AccountNotFoundException, TagNotFoundException;

    BigDecimal getLifetimeTutorEarning(Long requestingAccountId, Long tutorId) throws AccountPermissionDeniedException, AccountNotFoundException;

    BigDecimal getCurrentMonthTutorEarning(Long requestingAccountId, Long tutorId) throws AccountPermissionDeniedException, AccountNotFoundException;

    List<MonthlyEarningResp> getMonthlyTutorEarningForLastYear(Long requestingAccountId, Long tutorId) throws AccountPermissionDeniedException, AccountNotFoundException;

    List<CourseWithEarningResp> getLifetimeHighestEarningCourses(Long requestingId) throws AccountNotFoundException, AccountPermissionDeniedException;

    List<CourseWithEarningResp> getCurrentMonthHighestEarningCourses(Long requestingId) throws AccountNotFoundException, AccountPermissionDeniedException;

    List<TutorWithEarningResp> getLifetimeHighestEarningTutors(Long requestingId) throws AccountNotFoundException, AccountPermissionDeniedException;

    List<TutorWithEarningResp> getCurrentMonthHighestEarningTutors(Long requestingId) throws AccountNotFoundException, AccountPermissionDeniedException;

    BigDecimal getLastMonthCourseEarning(Long requestingAccountId, Long tutorId) throws AccountPermissionDeniedException, AccountNotFoundException,  CourseNotFoundException;

    BigDecimal getNumEnrolledCurrentMonthByCourseId(Long requestingAccountId, Long courseId) throws AccountPermissionDeniedException, AccountNotFoundException , CourseNotFoundException;

    BigDecimal getNumEnrolledLastMonthByCourseId(Long requestingAccountId, Long courseId) throws AccountPermissionDeniedException, AccountNotFoundException , CourseNotFoundException;

    BigDecimal getNumStudentsCompleted(Long requestingAccountId, Long courseId) throws AccountPermissionDeniedException, AccountNotFoundException , CourseNotFoundException;

    BigDecimal getLastMonthTutorEarnings(Long requestingAccountId, Long tutorId) throws AccountPermissionDeniedException, AccountNotFoundException;

    Integer getCurrentMonthEnrollmentCount(Long requestingAccountId) throws AccountNotFoundException, AccountPermissionDeniedException;

    Integer getPreviousMonthEnrollmentCount(Long requestingAccountId) throws AccountNotFoundException, AccountPermissionDeniedException;

    List<TransactionWithParticularsResp> getTransactionsWithParticularsForLastThirtyDays(Long requestingAccountId) throws AccountNotFoundException, AccountPermissionDeniedException;
}
