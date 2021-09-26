package com.spring.kodo.repository;

import com.spring.kodo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    @Query(value = "SELECT * FROM Account a JOIN Account_Courses ac ON a.account_id = ac.account_id WHERE ac.course_id = :courseId", nativeQuery = true)
    Optional<Account> findByCourseId(@Param("courseId") Long courseId);

    @Query(value = "SELECT * FROM Account a JOIN Account_Enrolled_Courses aec ON a.account_id = aec.account_id WHERE aec.enrolled_course_id = :enrolledCourseId", nativeQuery = true)
    Optional<Account> findByEnrolledCourseId(@Param("enrolledCourseId") Long enrolledCourseId);

    @Query(value = "SELECT * FROM Account a JOIN Account_Student_Attempts asa ON a.account_id = asa.account_id WHERE asa.student_attempt_id = :studentAttemptId", nativeQuery = true)
    Optional<Account> findByStudentAttemptId(@Param("studentAttemptId") Long studentAttemptId);

    @Query(value = "SELECT a.* FROM Account a JOIN Account_Interests ai ON a.account_id = ai.account_id WHERE ai.tag_id = :tagId", nativeQuery = true)
    List<Account> findAllAccountsByTagId(@Param("tagId") Long tagId);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query(value = "SELECT COUNT(*) FROM Account a JOIN Account_Courses ac on a.account_id = ac.account_id JOIN Course c ON c.course_id = ac.course_id JOIN enrolled_course ec ON ec.parent_course_course_id = c.course_id WHERE a.account_id = :accountId", nativeQuery = true)
    Optional<Integer> getTotalEnrollmentCountByAccountId(@Param("accountId") Long accountId);

    @Query(value = "SELECT COUNT(*) FROM Account a JOIN Account_Courses ac on a.account_id = ac.account_id JOIN Course c ON c.course_id = ac.course_id WHERE a.account_id = :accountId AND c.is_enrollment_active = TRUE", nativeQuery = true)
    Optional<Integer> getTotalPublishedCourseCountByAccountId(@Param("accountId") Long accountId);

    @Query(value = "SELECT COUNT(*) FROM Account a JOIN Account_Courses ac on a.account_id = ac.account_id JOIN Course c ON c.course_id = ac.course_id WHERE a.account_id = :accountId", nativeQuery = true)
    Optional<Integer> getTotalCourseCountByAccountId(@Param("accountId") Long accountId);
}
