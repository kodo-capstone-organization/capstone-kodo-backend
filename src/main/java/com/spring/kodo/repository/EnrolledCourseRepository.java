package com.spring.kodo.repository;

import com.spring.kodo.entity.EnrolledCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnrolledCourseRepository extends JpaRepository<EnrolledCourse, Long>
{
    Optional<EnrolledCourse> findByCourseRating(int courseRating);

    @Query(value = "SELECT * FROM Enrolled_Course ec JOIN Course c JOIN Account_Enrolled_Courses aec ON ec.parent_course_course_id = c.course_id AND ec.enrolled_course_id = aec.enrolled_course_id WHERE c.name LIKE :courseName AND aec.account_id = :studentId", nativeQuery = true)
    Optional<EnrolledCourse> findByStudentIdAndCourseName(@Param("studentId") Long studentId, @Param("courseName") String courseName);
}
