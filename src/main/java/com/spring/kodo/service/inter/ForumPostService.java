package com.spring.kodo.service.inter;

import com.spring.kodo.entity.ForumPost;
import com.spring.kodo.util.exception.ForumPostNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;

import java.util.List;

public interface ForumPostService {

    ForumPost createNewForumPost (ForumPost newForumPost) throws InputDataValidationException;

    ForumPost getForumPostByForumPostId(Long forumPostId) throws ForumPostNotFoundException;

    List<ForumPost> getAllForumPosts();

    ForumPost updateForumPost(Long forumPostId, ForumPost updatedForumPost) throws ForumPostNotFoundException;

    Boolean deleteForumPost (Long forumPostId) throws ForumPostNotFoundException;
}
