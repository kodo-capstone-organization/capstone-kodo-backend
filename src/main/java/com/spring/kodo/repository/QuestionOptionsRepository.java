package com.spring.kodo.repository;

import com.spring.kodo.entity.QuestionOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionOptionsRepository extends JpaRepository<QuestionOptions, Long>
{
}