package com.spring.kodo.repository;

import com.spring.kodo.entity.Transaction;
import com.spring.kodo.restentity.response.CourseWithEarningResp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>
{
    Boolean existsByStripeTransactionId(String stripeTransactionId);

//    // Used to get Kodo lifetime earnings
//    @Query(value = "SELECT SUM(t.platform_fee) FROM `Transaction` t", nativeQuery = true)
//    BigDecimal getAllPlatformEarning();
//
//
//    // Used to get all transactions by course id
//    @Query(value = "SELECT * FROM `Transaction` t JOIN Course c ON t.course_course_id = c.course_id WHERE c.course_id = :courseId", nativeQuery = true)
//    List<Transaction> getAllTransactionByCourseId(@Param("courseId") Long courseId);
//
//    @Query(value = "SELECT * FROM `Transaction` t JOIN Course c ON t.course_course_id = c.course_id WHERE c.course_id = :courseId AND YEAR(t.date_time_of_transaction) = :inputYear AND MONTH(t.date_time_of_transaction) = :inputMonth", nativeQuery = true)
//    List<Transaction> getMonthlyTransactionByCourseId(@Param("courseId") Long courseId, @Param("year") Year inputYear, @Param("month") Month inputMonth);
//
//    @Query(value = "SELECT * FROM `Transaction` t JOIN Course c ON t.course_course_id = c.course_id WHERE c.course_id = :courseId AND YEAR(t.date_time_of_transaction) = :inputYear", nativeQuery = true)
//    List<Transaction> getYearlyTransactionByCourseId(@Param("courseId") Long courseId, @Param("year") Year inputYear);
//
//
//    // Used to get all transactions by payee/tutor
//    @Query(value = "SELECT * FROM `Transaction` t JOIN Account a ON t.payee_account_id = :payeeId", nativeQuery = true)
//    List<Transaction> getAllTransactionByPayeeId(@Param("payeeId") Long payeeId);
//
//    @Query(value = "SELECT * FROM `Transaction` t JOIN Account a ON t.payee_account_id = :payeeId WHERE YEAR(t.date_time_of_transaction) = :inputYear AND MONTH(t.date_time_of_transaction) = :inputMonth", nativeQuery = true)
//    List<Transaction> getMonthlyTransactionByPayeeId(@Param("payeeId") Long payeeId, @Param("year") Year inputYear, @Param("month") Month inputMonth);
//
//    @Query(value = "SELECT * FROM `Transaction` t JOIN Account a ON t.payee_account_id = :payeeId WHERE YEAR(t.date_time_of_transaction) = :inputYear", nativeQuery = true)
//    List<Transaction> getYearlyTransactionByPayeeId(@Param("payeeId") Long payeeId, @Param("year") Year inputYear);
//
//    // Used to get all transactions by payer/student
//    @Query(value = "SELECT * FROM `Transaction` t JOIN Account a ON t.payer_account_id = a.account_id WHERE t.payer_account_id = :payerId", nativeQuery = true)
//    List<Transaction> getAllTransactionByPayerId(@Param("payerId") Long payerId);
//
//
//    // Used to generate earnings summary per course
//    @Query(value = "SELECT c.course_id, c.name, SUM(t.tutor_payout) AS earnings FROM `Transaction` t JOIN Course c ON t.course_course_id = c.course_id GROUP BY c.course_id, c.name", nativeQuery = true)
//    List<CourseWithEarningResp> getAllTransactionsGroupbyCourse();
//
//    @Query(value = "SELECT c.course_id, c.name, SUM(t.tutor_payout) AS earnings FROM `Transaction` t JOIN Course c ON t.course_course_id = c.course_id WHERE YEAR(t.date_time_of_transaction) = :inputYear AND MONTH(t.date_time_of_transaction) = :inputMonth GROUP BY c.course_id, c.name", nativeQuery = true)
//    List<CourseWithEarningResp> getAllTransactionsByGroupbyCourseByMonth(@Param("year") Year inputYear, @Param("month") Month inputMonth);
//
//    @Query(value = "SELECT c.course_id, c.name, SUM(t.tutor_payout) AS earnings FROM `Transaction` t JOIN Course c ON t.course_course_id = c.course_id WHERE YEAR(t.date_time_of_transaction) = :inputYear GROUP BY c.course_id, c.name", nativeQuery = true)
//    List<CourseWithEarningResp> getAllTransactionsByGroupbyCourseByYear(@Param("year") Year inputYear);
}
