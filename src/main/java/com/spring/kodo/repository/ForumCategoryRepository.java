package com.spring.kodo.repository;

import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.ForumCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForumCategoryRepository extends JpaRepository<ForumCategory, Long>
{
    Optional<ForumCategory> findByName(String name);

    @Query(value ="SELECT fc.* FROM forum_category fc WHERE course_course_id = :courseId" , nativeQuery = true)
    List<ForumCategory> findByCourseId(@Param("courseId") Long courseId);
}
