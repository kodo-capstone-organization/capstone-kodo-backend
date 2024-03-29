package com.spring.kodo.repository;

import com.spring.kodo.entity.QuizQuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizQuestionOptionRepository extends JpaRepository<QuizQuestionOption, Long>
{
    Optional<QuizQuestionOption> findByLeftContent(String leftContent);

    Optional<QuizQuestionOption> findByRightContent(String rightContent);
}