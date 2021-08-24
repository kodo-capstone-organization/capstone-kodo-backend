package com.spring.kodo.repository;

import com.spring.kodo.entity.StudentAttemptAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAttemptAnswerRepository extends JpaRepository<StudentAttemptAnswer, Long>
{
}
