package com.spring.kodo.repository;

import com.spring.kodo.entity.ForumCategory;
import com.spring.kodo.entity.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForumPostRepository extends JpaRepository<ForumPost, Long>
{
    @Query(value = "SELECT fp.* FROM forum_post fp JOIN forum_thread_forum_posts ftfp ON fp.forum_post_id = ftfp.forum_post_id WHERE fp.parent_forum_post_forum_post_id IS NULL AND ftfp.forum_thread_id = :forumThreadId", nativeQuery = true)
    List<ForumPost> findAllByNullParentPostAndForumThreadId(@Param("forumThreadId") Long forumThreadId);

    @Query(value = "SELECT fp.* FROM forum_post fp JOIN forum_thread_forum_posts ftfp ON fp.forum_post_id = ftfp.forum_post_id WHERE ftfp.forum_thread_id = :forumThreadId AND fp.is_reported = true", nativeQuery = true)
    List<ForumPost> findAllReportedForumPostsByThreadId(@Param("forumThreadId") Long forumThreadId);
}
