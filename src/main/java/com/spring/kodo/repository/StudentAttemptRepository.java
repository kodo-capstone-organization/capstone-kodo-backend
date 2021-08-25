package com.spring.kodo.repository;

import com.spring.kodo.entity.QuizQuestion;
import com.spring.kodo.entity.StudentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAttemptRepository extends JpaRepository<StudentAttempt, Long>
{
}