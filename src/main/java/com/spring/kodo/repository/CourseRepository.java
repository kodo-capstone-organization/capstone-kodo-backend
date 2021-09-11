package com.spring.kodo.repository;

import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>
{
    Optional<Course> findByName(String name);

    @Query(value = "SELECT * FROM Course c JOIN Course_Lessons cl ON c.course_id = cl.course_id WHERE cl.lesson_id = :lessonId", nativeQuery = true)
    Optional<Course> findByLessonId(@Param("lessonId") Long lessonId);

    @Query("SELECT c FROM Course c WHERE c.isEnrollmentActive = TRUE")
    List<Course> findAllWithActiveEnrollment();

    @Query(value = "SELECT * FROM Tag t JOIN Course_Course_Tags cct JOIN Course c ON t.tag_id = cct.tag_id AND c.course_id = cct.course_id WHERE t.title = :tagTitle", nativeQuery = true)
    List<Course> findAllCoursesByTagTitle(@Param("tagTitle") String tagTitle);

    @Query("SELECT c FROM Course c WHERE c.name LIKE %:keyword%")
    List<Course> findAllCoursesByKeyword(@Param("keyword") String keyword);

    @Query("SELECT a.courses FROM Account a WHERE a.accountId = :tutorId")
    List<Course> findAllCoursesByTutorId(@Param("tutorId") Long tutorId);

    @Query(value = "SELECT DISTINCT * FROM Course c JOIN Course_Course_Tags cct ON c.course_id = cct.course_id WHERE cct.tag_id IN ((:allTagIds))", nativeQuery = true)
    List<Course> findAllCoursesByAllTagIds(@Param("allTagIds") List<Long> allTagIds);

    @Query(value = "SELECT AVG(ec.course_rating) FROM Course c JOIN Enrolled_Course ec WHERE c.course_id = :courseId", nativeQuery = true)
    Double findCourseRatingByCourseId(@Param("courseId") Long courseId);

    @Query(value = "SELECT SUM(t.course_price) FROM Transaction t WHERE t.course_course_id = :courseId", nativeQuery = true)
    BigDecimal findTotalEarningsByCourseId(@Param("courseId") Long courseId);

    @Query(value = "SELECT SUM(t.course_price) FROM Transaction t WHERE t.course_course_id = :courseId AND YEAR(t.date_time_of_transaction) BETWEEN :year AND YEAR(DATE_ADD(:year, INTERVAL 1 YEAR))", nativeQuery = true)
    BigDecimal findTotalEarningsByCourseIdAndYear(@Param("courseId") Long courseId, @Param("year") Integer year);
}
