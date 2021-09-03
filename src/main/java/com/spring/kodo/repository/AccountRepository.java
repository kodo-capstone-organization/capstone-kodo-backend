package com.spring.kodo.repository;

import com.spring.kodo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    // TODO check if legit works
    @Query("SELECT COUNT(sa) FROM Account a JOIN a.studentAttempts sa WHERE a.accountId = :accountId AND sa.quiz.contentId = :quizContentId")
    Long getNumberOfStudentAttemptsByStudentForQuiz(
            @Param("quizContentId") Long quizContentId,
            @Param("accountId") Long accountId
    );
}
