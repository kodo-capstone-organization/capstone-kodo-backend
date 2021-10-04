package com.spring.kodo.repository;

import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.EnrolledCourse;
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
    @Query(value = "SELECT * FROM enrolled_lesson el JOIN enrolled_lesson_enrolled_contents elec ON el.enrolled_lesson_id = elec.enrolled_lesson_id WHERE elec.enrolled_content_id = :enrolledContentId", nativeQuery = true)
    Optional<EnrolledLesson> findByEnrolledContentId(@Param("enrolledContentId") Long enrolledContentId);

    @Query(value = "SELECT el FROM enrolled_lesson el WHERE el.parentLesson.lessonId = :parentLessonId", nativeQuery = true)
    List<EnrolledLesson> findAllEnrolledLessonsByParentLessonId(@Param("parentLessonId") Long parentLessonId);

    @Query(value = "SELECT * FROM account_enrolled_courses aec JOIN enrolled_course_enrolled_lessons ecel JOIN enrolled_lesson el ON aec.enrolled_course_id = ecel.enrolled_course_id AND ecel.enrolled_lesson_id = el.enrolled_lesson_id WHERE aec.account_id = :studentId AND el.parent_lesson_lesson_id = :lessonId", nativeQuery = true)
    Optional<EnrolledLesson> findByStudentIdAndLessonId(@Param("studentId") Long studentId, @Param("lessonId") Long lessonId);
}
