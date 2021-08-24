package com.spring.kodo.repository;

import com.spring.kodo.entity.QuizQuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizQuestionOptionRepository extends JpaRepository<QuizQuestionOption, Long>
{
}