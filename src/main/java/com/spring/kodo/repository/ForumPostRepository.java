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
    @Query(value = "SELECT * FROM forum_post fp WHERE fp.parent_forum_post_forum_post_id = :parentForumPostId", nativeQuery = true)
    List<ForumPost> findAllByParentForumPostId(@Param("parentForumPostId") Long parentForumPostId);
}
