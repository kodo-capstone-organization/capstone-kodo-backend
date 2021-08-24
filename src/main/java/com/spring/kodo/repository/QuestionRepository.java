package com.spring.kodo.repository;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>
{
}