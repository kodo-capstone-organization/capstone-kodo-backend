package com.spring.kodo.repository;

import com.spring.kodo.entity.EnrolledCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnrolledCourseRepository extends JpaRepository<EnrolledCourse, Long>
{
    Optional<EnrolledCourse> findByCourseRating(int courseRating);
}
