package com.spring.kodo.repository;

import com.spring.kodo.entity.EnrolledCourse;
import com.spring.kodo.entity.EnrolledLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnrolledCourseRepository extends JpaRepository<EnrolledCourse, Long>
{
    Optional<EnrolledCourse> findByCourseRating(int courseRating);

    @Query(value = "SELECT * FROM Enrolled_Course ec JOIN Enrolled_Course_Enrolled_Lessons ecel ON ec.enrolled_course_id = ecel.enrolled_course_id WHERE ecel.enrolled_lesson_id = :enrolledLessonId", nativeQuery = true)
    Optional<EnrolledCourse> findByEnrolledLessonId(@Param("enrolledLessonId") Long enrolledLessonId);

    @Query(value = "SELECT * FROM Enrolled_Course ec JOIN Course c JOIN Account_Enrolled_Courses aec ON ec.parent_course_course_id = c.course_id AND ec.enrolled_course_id = aec.enrolled_course_id WHERE c.name LIKE :courseName AND aec.account_id = :studentId", nativeQuery = true)
    Optional<EnrolledCourse> findByStudentIdAndCourseName(@Param("studentId") Long studentId, @Param("courseName") String courseName);

    @Query(value = "SELECT * FROM Enrolled_Course ec JOIN Course c JOIN Account_Enrolled_Courses aec ON ec.parent_course_course_id = c.course_id AND ec.enrolled_course_id = aec.enrolled_course_id WHERE c.course_id LIKE :courseId AND aec.account_id = :studentId", nativeQuery = true)
    Optional<EnrolledCourse> findByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
}
