package com.spring.kodo.repository;

import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.EnrolledLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrolledLessonRepository extends JpaRepository<EnrolledLesson, Long>
{
    @Query(value = "SELECT * FROM Enrolled_Lesson el JOIN Enrolled_Lesson_Enrolled_Contents elec ON el.enrolled_lesson_id = elec.enrolled_lesson_id WHERE elec.enrolled_content_id = :enrolledContentId", nativeQuery = true)
    Optional<EnrolledLesson> findByEnrolledContentId(@Param("enrolledContentId") Long enrolledContentId);

    @Query("SELECT el FROM EnrolledLesson el WHERE el.parentLesson.lessonId = :parentLessonId")
    List<EnrolledLesson> findAllEnrolledLessonsByParentLessonId(@Param("parentLessonId") Long parentLessonId);
}
