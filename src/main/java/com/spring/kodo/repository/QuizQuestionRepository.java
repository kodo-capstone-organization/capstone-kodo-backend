package com.spring.kodo.repository;

import com.spring.kodo.entity.QuizQuestion;
import com.spring.kodo.util.enumeration.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long>
{
    Optional<QuizQuestion> findByContent(String content);

    Optional<QuizQuestion> findByQuestionType(QuestionType questionType);

    @Query(value = "SELECT qq.* FROM quiz_question qq WHERE Quiz_Content_Id = :quizId", nativeQuery = true)
    List<QuizQuestion> findAllQuizQuestionsByQuizId(@Param("quizId") Long quizId);

    @Query(value =
            "SELECT qq.*\n" +
            "FROM quiz_question qq\n" +
            "    JOIN lesson_contents lc\n" +
            "    JOIN course_lessons cl\n" +
            "    JOIN account_courses ac\n" +
            "        ON qq.quiz_content_id = lc.content_id\n" +
            "        AND lc.lesson_id = cl.lesson_id\n" +
            "        AND cl.course_id = ac.course_id\n" +
            "WHERE ac.account_id = :tutorId", nativeQuery = true)
    List<QuizQuestion> findAllQuizQuestionsByTutorId(@Param("tutorId") Long tutorId);

    @Query(value = "SELECT COUNT(saq) > 0\n" +
            "FROM student_attempt_question saq\n" +
            "WHERE saq.quizQuestion.quizQuestionId = :quizQuestionId", nativeQuery = true)
    Boolean containsStudentAttemptQuestions(@Param("quizQuestionId") Long quizQuestionId);
}