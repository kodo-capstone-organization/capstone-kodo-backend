package com.spring.kodo.repository;

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

    @Query(value = "SELECT fc.* FROM forum_category fc JOIN forum_category_forum_threads fcft ON fc.forum_category_id = fcft.forum_category_id WHERE fcft.forum_thread_id = :forumThreadId", nativeQuery = true)
    Optional<ForumCategory> findByForumThreadId(@Param("forumThreadId") Long forumThreadId);

    @Query(value = "SELECT fc.* FROM forum_category fc WHERE course_course_id = :courseId", nativeQuery = true)
    List<ForumCategory> findAllByCourseId(@Param("courseId") Long courseId);
}
