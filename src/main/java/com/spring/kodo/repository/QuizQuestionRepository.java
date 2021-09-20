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
}