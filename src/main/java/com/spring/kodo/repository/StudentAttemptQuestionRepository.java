package com.spring.kodo.repository;

import com.spring.kodo.entity.StudentAttempt;
import com.spring.kodo.entity.StudentAttemptQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAttemptQuestionRepository extends JpaRepository<StudentAttemptQuestion, Long>
{
}