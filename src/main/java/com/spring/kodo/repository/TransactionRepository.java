package com.spring.kodo.repository;

import com.spring.kodo.entity.Transaction;
import com.spring.kodo.restentity.response.CourseWithEarningResp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>
{
    Boolean existsByStripeTransactionId(String stripeTransactionId);

    // -- Used to get Kodo platform earnings for Admin portal
    @Query(value = "SELECT SUM(t.platform_fee) FROM `Transaction` t", nativeQuery = true)
    Optional<BigDecimal> getLifetimePlatformEarning();

    @Query(value = "SELECT SUM(t.platform_fee) FROM `Transaction` t WHERE MONTH(t.date_time_of_transaction) = MONTH(NOW())", nativeQuery = true)
    Optional<BigDecimal> getCurrentMonthPlatformEarning();

    @Query(value = "SELECT SUM(t.platform_fee) FROM `Transaction` t WHERE MONTH(t.date_time_of_transaction) = MONTH(NOW() - 1)", nativeQuery = true)
    Optional<BigDecimal> getLastMonthPlatformEarning();

    @Query(value = "SELECT SUM(t.platform_fee) FROM `Transaction` t WHERE YEAR(t.date_time_of_transaction) = :inputYear AND MONTH(t.date_time_of_transaction) = :inputMonth", nativeQuery = true)
    Optional<BigDecimal> getMonthlyPlatformEarning(@Param("inputYear") int inputYear, @Param("inputMonth") int inputMonth);
    // --


    // -- Used to get course earnings for Admin portal
    @Query(value = "SELECT SUM(t.tutor_payout) FROM `Transaction` t WHERE t.course_course_id = :courseId", nativeQuery = true)
    Optional<BigDecimal> getLifetimeCourseEarning(@Param("courseId") Long courseId);

    @Query(value = "SELECT SUM(t.tutor_payout) FROM `Transaction` t WHERE t.course_course_id = :courseId AND MONTH(t.date_time_of_transaction) = MONTH(NOW()) AND YEAR(t.date_time_of_transaction) = YEAR(NOW())", nativeQuery = true)
    Optional<BigDecimal> getCurrentMonthCourseEarning(@Param("courseId") Long courseId);

    @Query(value = "SELECT SUM(t.tutor_payout) FROM `Transaction` t WHERE t.course_course_id = :courseId AND YEAR(t.date_time_of_transaction) = :inputYear AND MONTH(t.date_time_of_transaction) = :inputMonth", nativeQuery = true)
    Optional<BigDecimal> getMonthlyCourseEarningForLastYear(@Param("courseId") Long courseId, @Param("inputYear") int inputYear, @Param("inputMonth") int inputMonth);
    // --


    // -- Used to get tag earnings for Admin portal
    @Query(value = "SELECT SUM(t.tutor_payout) FROM `Transaction` t JOIN Course_Course_Tags cct ON t.course_course_id = cct.course_id WHERE cct.tag_id = :tagId", nativeQuery = true)
    Optional<BigDecimal> getLifetimeTagEarning(@Param("tagId") Long tagId);

    @Query(value = "SELECT SUM(t.tutor_payout) FROM `Transaction` t JOIN Course_Course_Tags cct ON t.course_course_id = cct.course_id WHERE cct.tag_id = :tagId AND MONTH(t.date_time_of_transaction) = MONTH(NOW()) AND YEAR(t.date_time_of_transaction) = YEAR(NOW())", nativeQuery = true)
    Optional<BigDecimal> getCurrentMonthTagEarning(@Param("tagId") Long tagId);

    @Query(value = "SELECT SUM(t.tutor_payout) FROM `Transaction` t JOIN Course_Course_Tags cct ON t.course_course_id = cct.course_id WHERE cct.tag_id = :tagId AND YEAR(t.date_time_of_transaction) = :inputYear AND MONTH(t.date_time_of_transaction) = :inputMonth", nativeQuery = true)
    Optional<BigDecimal> getMonthlyTagEarningForLastYear(@Param("tagId") Long tagId, @Param("inputYear") int inputYear, @Param("inputMonth") int inputMonth);
    // --


    // Used to get all transactions by course id
    @Query(value = "SELECT * FROM `Transaction` t JOIN Course c ON t.course_course_id = c.course_id WHERE c.course_id = :courseId", nativeQuery = true)
    List<Transaction> getAllTransactionByCourseId(@Param("courseId") Long courseId);

    @Query(value = "SELECT * FROM `Transaction` t JOIN Course c ON t.course_course_id = c.course_id WHERE c.course_id = :courseId AND YEAR(t.date_time_of_transaction) = :inputYear AND MONTH(t.date_time_of_transaction) = :inputMonth", nativeQuery = true)
    List<Transaction> getMonthlyTransactionByCourseId(@Param("courseId") Long courseId, @Param("inputYear") Year inputYear, @Param("inputMonth") int inputMonth);

    @Query(value = "SELECT * FROM `Transaction` t JOIN Course c ON t.course_course_id = c.course_id WHERE c.course_id = :courseId AND YEAR(t.date_time_of_transaction) = :inputYear", nativeQuery = true)
    List<Transaction> getYearlyTransactionByCourseId(@Param("courseId") Long courseId, @Param("inputYear") Year inputYear);

    @Query(value = "SELECT SUM(t.tutor_payout) FROM `Transaction` t JOIN Course c ON t.course_course_id = c.course_id WHERE c.course_id = :courseId", nativeQuery = true)
    Optional<BigDecimal> getLifetimeTutorPayoutByCourseId(@Param("courseId") Long courseId);

    @Query(value = "SELECT SUM(t.tutor_payout) FROM `Transaction` t JOIN Course c ON t.course_course_id = c.course_id WHERE c.course_id = :courseId AND YEAR(t.date_time_of_transaction) = YEAR(NOW()) AND MONTH(t.date_time_of_transaction) = MONTH(NOW())", nativeQuery = true)
    Optional<BigDecimal> getCurrentMonthTutorPayoutByCourseId(@Param("courseId") Long courseId);

    @Query(value = "SELECT ROUND(AVG(Month_Sum), 2) FROM (SELECT SUM(t.tutor_payout) AS Month_Sum FROM `Transaction` t JOIN Course c ON t.course_course_id = :courseId WHERE c.course_id = :courseId GROUP BY YEAR(t.date_time_of_transaction), MONTH(t.date_time_of_transaction)) AS Inner_Query", nativeQuery = true)
    Optional<BigDecimal> getAverageMonthlyEarningByCourseId(@Param("courseId") Long courseId);

    @Query(value = "SELECT SUM(t.tutor_payout) As MonthSum, YEAR(t.date_time_of_transaction) As DisYear, MONTH(t.date_time_of_transaction) As DisMonth FROM `Transaction` t JOIN Course c ON t.course_course_id = :courseId WHERE c.course_id = :courseId GROUP BY YEAR(t.date_time_of_transaction), MONTH(t.date_time_of_transaction) ORDER BY SUM(t.tutor_payout) DESC LIMIT 1", nativeQuery = true)
    Map<String, Object> getHighestEarningMonthWithValue(@Param("courseId") Long courseId);

    @Query(value = "SELECT SUM(t.tutor_payout) FROM `Transaction` t JOIN Course c ON t.course_course_id = c.course_id WHERE c.course_id = :courseId AND YEAR(t.date_time_of_transaction) = :inputYear AND MONTH(t.date_time_of_transaction) = :inputMonth", nativeQuery = true)
    Optional<BigDecimal> getTutorPayoutByMonthByCourseId(@Param("courseId") Long courseId, @Param("inputYear") int inputYear, @Param("inputMonth") int inputMonth);

    // Used to get all transactions by payee/tutor
    @Query(value = "SELECT * FROM `Transaction` t JOIN Account a ON t.payee_account_id = a.account_id AND t.payee_account_id = :payeeId", nativeQuery = true)
    List<Transaction> getAllTransactionByPayeeId(@Param("payeeId") Long payeeId);

    @Query(value = "SELECT SUM(t.tutor_payout) FROM `Transaction` t JOIN Account a ON t.payee_account_id = a.account_id AND t.payee_account_id = :payeeId", nativeQuery = true)
    Optional<BigDecimal> getLifetimeEarningsByPayeeId(@Param("payeeId") Long payeeId);

    @Query(value = "SELECT SUM(t.tutor_payout) FROM `Transaction` t JOIN Account a ON t.payee_account_id = a.account_id AND t.payee_account_id = :payeeId WHERE YEAR(t.date_time_of_transaction) = :inputYear AND MONTH(t.date_time_of_transaction) = :inputMonth", nativeQuery = true)
    Optional<BigDecimal> getMonthlyTransactionByPayeeId(@Param("payeeId") Long payeeId, @Param("inputYear") int inputYear, @Param("inputMonth") int inputMonth);

    @Query(value = "SELECT SUM(t.tutor_payout) FROM `Transaction` t JOIN Account a ON t.payee_account_id = a.account_id AND t.payee_account_id = :payeeId WHERE YEAR(t.date_time_of_transaction) = YEAR(NOW()) AND MONTH(t.date_time_of_transaction) = MONTH(NOW())", nativeQuery = true)
    Optional<BigDecimal> getCurrentMonthEarningsByPayeeId(@Param("payeeId") Long payeeId);

    @Query(value = "SELECT * FROM `Transaction` t JOIN Account a ON t.payee_account_id = a.account_id AND t.payee_account_id = :payeeId WHERE YEAR(t.date_time_of_transaction) = :inputYear", nativeQuery = true)
    List<Transaction> getYearlyTransactionByPayeeId(@Param("payeeId") Long payeeId, @Param("inputYear") Year inputYear);


    // Used to get all transactions by payer/student
    @Query(value = "SELECT * FROM `Transaction` t JOIN Account a ON t.payer_account_id = a.account_id WHERE t.payer_account_id = :payerId", nativeQuery = true)
    List<Transaction> getAllTransactionByPayerId(@Param("payerId") Long payerId);


    // Used to generate earnings summary per course
    @Query(value = "SELECT c.course_id, c.name, SUM(t.tutor_payout) AS earnings FROM `Transaction` t JOIN Course c ON t.course_course_id = c.course_id GROUP BY c.course_id, c.name", nativeQuery = true)
    List<CourseWithEarningResp> getAllTransactionsGroupbyCourse();

    @Query(value = "SELECT c.course_id, c.name, SUM(t.tutor_payout) AS earnings FROM `Transaction` t JOIN Course c ON t.course_course_id = c.course_id WHERE YEAR(t.date_time_of_transaction) = :inputYear AND MONTH(t.date_time_of_transaction) = :inputMonth GROUP BY c.course_id, c.name", nativeQuery = true)
    List<CourseWithEarningResp> getAllTransactionsByGroupbyCourseByMonth(@Param("inputYear") Year inputYear, @Param("inputMonth") int inputMonth);

    @Query(value = "SELECT c.course_id, c.name, SUM(t.tutor_payout) AS earnings FROM `Transaction` t JOIN Course c ON t.course_course_id = c.course_id WHERE YEAR(t.date_time_of_transaction) = :inputYear GROUP BY c.course_id, c.name", nativeQuery = true)
    List<CourseWithEarningResp> getAllTransactionsByGroupbyCourseByYear(@Param("inputYear") Year inputYear);
}
