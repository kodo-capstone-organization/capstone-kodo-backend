package com.spring.kodo.repository;

import com.spring.kodo.entity.CompletedLesson;
import com.spring.kodo.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long>
{
    Optional<Quiz> findByName(String name);
}
