package com.spring.kodo.repository;

import com.spring.kodo.entity.Quiz;
import com.spring.kodo.entity.rest.response.QuizWithStudentAttemptCountResp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long>
{
    Optional<Quiz> findByName(String name);

    @Query(value = "SELECT * FROM kodo.content c JOIN kodo.quiz q JOIN kodo.enrolled_content ec ON c.content_id = q.content_id AND q.content_id = ec.parent_content_content_id WHERE ec.enrolled_content_id = :enrolledContentId", nativeQuery = true)
    Optional<Quiz> findByEnrolledContentId(@Param("enrolledContentId") Long enrolledContentId);

    @Query(value =
            "SELECT\n" +
            "       q.content_id as 'contentId',\n" +
            "       q.time_limit AS 'timeLimit',\n" +
            "       q.max_attempts_per_student AS 'maxAttemptsPerStudent',\n" +
            "       (q.max_attempts_per_student - COUNT(sa.student_attempt_id)) AS 'studentAttemptCount'\n" +
            "FROM enrolled_lesson el\n" +
            "    JOIN lesson_contents lc\n" +
            "    JOIN quiz q\n" +
            "    JOIN student_attempt sa\n" +
            "ON el.parent_lesson_lesson_id = lc.lesson_id\n" +
            "AND lc.content_id = q.content_id\n" +
            "AND q.content_id = sa.quiz_content_id\n" +
            "WHERE el.enrolled_lesson_id = :enrolledLessonId\n" +
            "GROUP BY q.content_id",
            nativeQuery = true)
    List<QuizWithStudentAttemptCountResp> findAllQuizzesWithStudentAttemptCountByEnrolledLessonId(@Param("enrolledLessonId") Long enrolledLessonId);
}
