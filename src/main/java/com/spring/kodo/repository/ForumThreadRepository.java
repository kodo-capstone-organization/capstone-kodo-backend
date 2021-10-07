package com.spring.kodo.repository;

import com.spring.kodo.entity.ForumThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ForumThreadRepository extends JpaRepository<ForumThread, Long>
{
    Optional<ForumThread> findByName(String name);

    @Query(value = "SELECT ft.* FROM forum_thread ft JOIN forum_thread_forum_posts ftfp ON ft.forum_thread_id = ftfp.forum_thread_id WHERE ftfp.forum_post_id = :forumPostId", nativeQuery = true)
    Optional<ForumThread> findByForumPostId(@Param("forumPostId") Long forumPostId);
}