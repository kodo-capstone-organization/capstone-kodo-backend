package com.spring.kodo.repository;

import com.spring.kodo.entity.CompletedLesson;
import com.spring.kodo.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long>
{
    // Basically the EntityManager of Account
    // Provides methods out-of-the-box. E.g.:
    // 1 - save(S entity)
    // 2 - findById(ID id)
    // 3 - findOne()
    // 4 - findAll()
    // For full method list, ctrl + click into JpaRepository
    // Or visit: https://docs.spring.io/autorepo/docs/spring-data-jpa/current/api/org/springframework/data/jpa/repository/support/SimpleJpaRepository.html

    Optional<Quiz> findByName(String name);
}
