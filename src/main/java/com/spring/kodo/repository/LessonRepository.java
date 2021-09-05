package com.spring.kodo.repository;

import com.spring.kodo.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long>
{
    Optional<Lesson> findByName(String name);

    @Query(value = "SELECT * FROM Lesson l JOIN Lesson_Contents lc ON l.lesson_id = lc.lesson_id WHERE lc.content_id = :contentId", nativeQuery = true)
    Optional<Lesson> findByContentId(@Param("contentId") Long contentId);
}
