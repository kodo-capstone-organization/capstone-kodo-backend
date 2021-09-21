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

    @Query(value = "SELECT qq.* FROM Quiz_Question qq WHERE Quiz_Content_Id = :quizId", nativeQuery = true)
    List<QuizQuestion> findAllQuizQuestionsByQuizId(@Param("quizId") Long quizId);

    @Query(value =
            "SELECT qq.*\n" +
            "FROM Quiz_Question qq\n" +
            "    JOIN Lesson_Contents lc\n" +
            "    JOIN Course_Lessons cl\n" +
            "    JOIN Account_Courses ac\n" +
            "        ON qq.quiz_content_id = lc.content_id\n" +
            "        AND lc.lesson_id = cl.lesson_id\n" +
            "        AND cl.course_id = ac.course_id\n" +
            "WHERE ac.account_id = :tutorId", nativeQuery = true)
    List<QuizQuestion> findAllQuizQuestionsByTutorId(@Param("tutorId") Long tutorId);
}