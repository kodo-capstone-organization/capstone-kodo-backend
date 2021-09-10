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
    @Query(value = "SELECT * FROM Enrolled_Lesson el JOIN Enrolled_Lesson_Enrolled_Contents elec ON el.enrolled_lesson_id = elec.enrolled_lesson_id WHERE elec.enrolled_content_id = :enrolledContentId", nativeQuery = true)
    Optional<EnrolledLesson> findByEnrolledContentId(@Param("enrolledContentId") Long enrolledContentId);

    @Query("SELECT el FROM EnrolledLesson el WHERE el.parentLesson.lessonId = :parentLessonId")
    List<EnrolledLesson> findAllEnrolledLessonsByParentLessonId(@Param("parentLessonId") Long parentLessonId);

    @Query(value = "SELECT * FROM Account_Enrolled_Courses aec JOIN Enrolled_Course_Enrolled_Lessons ecel JOIN Enrolled_Lesson el ON aec.enrolled_course_id = ecel.enrolled_course_id AND ecel.enrolled_lesson_id = el.enrolled_lesson_id WHERE aec.account_id = :studentId AND el.parent_lesson_lesson_id = :lessonId", nativeQuery = true)
    Optional<EnrolledLesson> findByStudentIdAndLessonId(@Param("studentId") Long studentId, @Param("lessonId") Long lessonId);
}
