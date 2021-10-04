package com.spring.kodo.repository;

import com.spring.kodo.entity.Course;
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

    @Query(value = "SELECT * FROM lesson l JOIN lesson_contents lc ON l.lesson_id = lc.lesson_id WHERE lc.content_id = :contentId", nativeQuery = true)
    Optional<Lesson> findByContentId(@Param("contentId") Long contentId);

    @Query(value = "SELECT l.* FROM lesson l JOIN lesson_contents lc JOIN enrolled_content ec ON l.lesson_id = lc.lesson_id AND lc.content_id = ec.parent_content_content_id WHERE ec.enrolled_content_id = :enrolledContentId", nativeQuery = true)
    Optional<Lesson> findByEnrolledContentId(@Param("enrolledContentId") Long enrolledContentId);

    @Query(value = "SELECT l.* FROM lesson l\n" +
            "JOIN enrolled_lesson el\n" +
            "    JOIN enrolled_lesson_enrolled_contents elec\n" +
            "    JOIN enrolled_content_student_attempts ecsa\n" +
            "ON l.lesson_id = el.parent_lesson_lesson_id\n" +
            "    AND el.enrolled_lesson_id = elec.enrolled_lesson_id\n" +
            "    AND elec.enrolled_content_id = ecsa.enrolled_content_id\n" +
            "WHERE ecsa.student_attempt_id = :studentAttemptId", nativeQuery = true)
    Optional<Lesson> findByStudentAttemptId(@Param("studentAttemptId") Long studentAttemptId);
}
