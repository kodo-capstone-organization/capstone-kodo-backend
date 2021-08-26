package com.spring.kodo.repository;

import com.spring.kodo.entity.QuizQuestion;
import com.spring.kodo.util.enumeration.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long>
{
    Optional<QuizQuestion> findByContent(String content);

    Optional<QuizQuestion> findByQuestionType(QuestionType questionType);
}