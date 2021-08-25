package com.spring.kodo.repository;

import com.spring.kodo.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long>
{
    Optional<Lesson> findByName(String name);
}
