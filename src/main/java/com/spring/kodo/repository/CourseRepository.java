package com.spring.kodo.repository;

import com.spring.kodo.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>
{
    Optional<Course> findByName(String name);

    @Query("SELECT c FROM Course c WHERE c.name LIKE %:keyword%")
    List<Course> findCoursesByKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM Tag t JOIN Course_Course_Tags cct JOIN Course c ON t.tag_id = cct.tag_id AND c.course_id = cct.course_id WHERE t.title = :tagTitle", nativeQuery = true)
    List<Course> findCoursesByTagTitle(@Param("tagTitle") String tagTitle);

    @Query("SELECT a.courses FROM Account a WHERE a.accountId = :tutorId")
    List<Course> findCoursesByTutorId(@Param("tutorId") Long tutorId);
}
