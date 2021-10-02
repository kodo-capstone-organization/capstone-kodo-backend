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

    @Query(value = "SELECT * FROM Lesson l JOIN Lesson_Contents lc ON l.lesson_id = lc.lesson_id WHERE lc.content_id = :contentId", nativeQuery = true)
    Optional<Lesson> findByContentId(@Param("contentId") Long contentId);

    @Query(value = "SELECT l.* FROM Lesson l JOIN Lesson_Contents lc JOIN Enrolled_Content ec ON l.lesson_id = lc.lesson_id AND lc.content_id = ec.parent_content_content_id WHERE ec.enrolled_content_id = :enrolledContentId", nativeQuery = true)
    Optional<Lesson> findByEnrolledContentId(@Param("enrolledContentId") Long enrolledContentId);

    @Query(value = "SELECT l.* FROM Lesson l\n" +
            "JOIN Enrolled_Lesson el\n" +
            "    JOIN Enrolled_Lesson_Enrolled_Contents elec\n" +
            "    JOIN Enrolled_Content_Student_Attempts ecsa\n" +
            "ON l.lesson_id = el.parent_lesson_lesson_id\n" +
            "    AND el.enrolled_lesson_id = elec.enrolled_lesson_id\n" +
            "    AND elec.enrolled_content_id = ecsa.enrolled_content_id\n" +
            "WHERE ecsa.student_attempt_id = :studentAttemptId", nativeQuery = true)
    Optional<Lesson> findByStudentAttemptId(@Param("studentAttemptId") Long studentAttemptId);
}
