package com.spring.kodo.repository;

import com.spring.kodo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>
{
    // Basically the EntityManager of Account
    // Provides methods out-of-the-box. E.g.:
    // 1 - save(S entity)
    // 2 - findById(ID id)
    // 3 - findOne()
    // 4 - findAll()
    // For full method list, ctrl + click into JpaRepository
    // Or visit: https://docs.spring.io/autorepo/docs/spring-data-jpa/current/api/org/springframework/data/jpa/repository/support/SimpleJpaRepository.html

    Optional<Account> findByUsername(String username);

    Optional<Account> findByName(String name);

    Optional<Account> findByEmail(String email);

    @Query(value = "SELECT * FROM account a JOIN kodo.account_courses ac ON a.account_id = ac.account_id WHERE ac.course_id = :courseId", nativeQuery = true)
    Optional<Account> findByCourseId(@Param("courseId") Long courseId);

    @Query(value = "SELECT * FROM account a JOIN kodo.account_courses ac JOIN kodo.course_lessons cl ON a.account_id = ac.account_id AND ac.course_id = cl.course_id WHERE cl.lesson_id = :lessonId", nativeQuery = true)
    Optional<Account> findByLessonId(@Param("lessonId") Long lessonId);

    @Query(value = "SELECT * FROM account a JOIN kodo.account_enrolled_courses aec ON a.account_id = aec.account_id WHERE aec.enrolled_course_id = :enrolledCourseId", nativeQuery = true)
    Optional<Account> findByEnrolledCourseId(@Param("enrolledCourseId") Long enrolledCourseId);

    @Query(value = "SELECT * FROM account a JOIN kodo.account_enrolled_courses aec JOIN kodo.enrolled_course_enrolled_lessons ecel ON a.account_id = aec.account_id AND aec.enrolled_course_id = ecel.enrolled_course_id WHERE ecel.enrolled_lesson_id = :enrolledLessonId", nativeQuery = true)
    Optional<Account> findByEnrolledLessonId(@Param("enrolledLessonId") Long enrolledLessonId);

    @Query(value = "SELECT * FROM account a JOIN kodo.account_enrolled_courses aec JOIN kodo.enrolled_course_enrolled_lessons ecel JOIN kodo.enrolled_lesson_enrolled_contents elec ON a.account_id = aec.account_id AND aec.enrolled_course_id = ecel.enrolled_course_id AND ecel.enrolled_lesson_id = elec.enrolled_lesson_id WHERE elec.enrolled_content_id = :enrolledContentId", nativeQuery = true)
    Optional<Account> findByEnrolledContentId(@Param("enrolledContentId") Long enrolledContentId);

    @Query(value = "SELECT * FROM account a\n" +
            "    JOIN account_enrolled_courses aec\n" +
            "    JOIN enrolled_course_enrolled_lessons ecel\n" +
            "    JOIN enrolled_lesson_enrolled_contents elec\n" +
            "    JOIN enrolled_content_student_attempts ecsa\n" +
            "        on a.account_id = aec.account_id\n" +
            "        AND aec.enrolled_course_id = ecel.enrolled_course_id\n" +
            "        AND ecel.enrolled_lesson_id = elec.enrolled_lesson_id\n" +
            "        AND elec.enrolled_content_id = ecsa.enrolled_content_id\n" +
            "WHERE ecsa.student_attempt_id = :studentAttemptId", nativeQuery = true)
    Optional<Account> findByStudentAttemptId(@Param("studentAttemptId") Long studentAttemptId);

    @Query(value = "SELECT * FROM account a JOIN account_courses ac ON a.account_id = ac.account_id\n"
                   + "    JOIN course_lessons cl ON ac.course_id = cl.course_id\n"
                   + "    JOIN lesson_contents lc ON cl.lesson_id = lc.lesson_id WHERE lc.content_id = :quizId", nativeQuery = true)
    Optional<Account> findByQuizId(@Param("quizId") Long quizId);

    @Query(value = "SELECT a.* FROM account a JOIN account_interests ai ON a.account_id = ai.account_id WHERE ai.tag_id = :tagId", nativeQuery = true)
    List<Account> findAllAccountsByTagId(@Param("tagId") Long tagId);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query(value = "SELECT COUNT(*) FROM account a JOIN account_courses ac on a.account_id = ac.account_id JOIN course c ON c.course_id = ac.course_id JOIN enrolled_course ec ON ec.parent_course_course_id = c.course_id WHERE a.account_id = :accountId", nativeQuery = true)
    Optional<Integer> getTotalEnrollmentCountByAccountId(@Param("accountId") Long accountId);

    @Query(value = "SELECT COUNT(*) FROM account a JOIN account_courses ac on a.account_id = ac.account_id JOIN course c ON c.course_id = ac.course_id WHERE a.account_id = :accountId AND c.is_enrollment_active = TRUE", nativeQuery = true)
    Optional<Integer> getTotalPublishedCourseCountByAccountId(@Param("accountId") Long accountId);

    @Query(value = "SELECT COUNT(*) FROM account a JOIN account_courses ac on a.account_id = ac.account_id JOIN course c ON c.course_id = ac.course_id WHERE a.account_id = :accountId", nativeQuery = true)
    Optional<Integer> getTotalCourseCountByAccountId(@Param("accountId") Long accountId);

    @Query(value = "SELECT COUNT(*) FROM `account_courses` ac WHERE ac.account_id = :tutorId", nativeQuery = true)
    Optional<Integer> findNumCoursesTaught(@Param("tutorId") Long tutorId);

    @Query(value = "SELECT COUNT(*) FROM `account_courses` ac JOIN `course` c ON ac.course_id = c.course_id WHERE account_id = :tutorId AND MONTH(date_time_of_creation) = MONTH(NOW()) AND YEAR(date_time_of_creation) = YEAR(NOW())", nativeQuery = true)
    Optional<Integer> findNumCoursesCreatedCurrentMonth(@Param("tutorId") Long tutorId);

    @Query(value = "SELECT COUNT(*) FROM `account_courses` ac JOIN `course` c ON ac.course_id = c.course_id WHERE account_id = :tutorId AND MONTH(date_time_of_creation) = MONTH(CURRENT_DATE - INTERVAL 1 MONTH) AND YEAR(date_time_of_creation) = YEAR(CURRENT_DATE - INTERVAL 1 MONTH)", nativeQuery = true)
    Optional<Integer> findNumCoursesCreatedLastMonth(@Param("tutorId") Long tutorId);

    @Query(value = "SELECT COUNT(*) FROM account a WHERE YEAR(a.date_time_of_creation) = YEAR(NOW()) AND MONTH(a.date_time_of_creation) = MONTH(NOW())", nativeQuery = true)
    Optional<Integer> getCurrentMonthNumberOfAccountCreation();

    @Query(value = "SELECT COUNT(*) FROM account a WHERE YEAR(a.date_time_of_creation) = YEAR(CURRENT_DATE - INTERVAL 1 MONTH) AND MONTH(a.date_time_of_creation) = MONTH(CURRENT_DATE - INTERVAL 1 MONTH)", nativeQuery = true)
    Optional<Integer> getPreviousMonthNumberOfAccountCreation();
}
