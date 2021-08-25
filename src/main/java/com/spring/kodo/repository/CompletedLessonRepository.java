package com.spring.kodo.repository;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.CompletedLesson;
import com.spring.kodo.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompletedLessonRepository extends JpaRepository<CompletedLesson, Long>
{
}
