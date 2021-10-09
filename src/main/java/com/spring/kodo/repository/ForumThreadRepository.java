package com.spring.kodo.repository;

import com.spring.kodo.entity.ForumThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ForumThreadRepository extends JpaRepository<ForumThread, Long>
{
    Optional<ForumThread> findByName(String name);

    @Query(value = "SELECT ft.* FROM forum_thread ft JOIN forum_thread_forum_posts ftfp ON ft.forum_thread_id = ftfp.forum_thread_id WHERE ftfp.forum_post_id = :forumPostId", nativeQuery = true)
    Optional<ForumThread> findByForumPostId(@Param("forumPostId") Long forumPostId);

    @Query(value = "SELECT * FROM forum_thread ft JOIN forum_category_forum_threads fcft ON ft.forum_thread_id = fcft.forum_thread_id WHERE fcft.forum_category_id = :forumCategoryId", nativeQuery = true)
    List<ForumThread> findAllByForumCategoryId(@Param("forumCategoryId") Long forumCategoryId);
}