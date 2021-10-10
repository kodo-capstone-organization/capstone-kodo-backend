package com.spring.kodo.service.inter;

import com.spring.kodo.entity.ForumPost;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface ForumPostService {

    ForumPost createNewForumPost(ForumPost newForumPost, Long accountId) throws InputDataValidationException, UnknownPersistenceException, AccountNotFoundException;

    ForumPost createNewForumPostReply(ForumPost newForumPostReply, Long accountId, Long parentForumPostId) throws UnknownPersistenceException, InputDataValidationException, AccountNotFoundException, ForumPostNotFoundException;

    ForumPost getForumPostByForumPostId(Long forumPostId) throws ForumPostNotFoundException;

    List<ForumPost> getAllForumPosts();

    List<ForumPost> getAllByNullParentPostAndForumThreadId(Long forumThreadId);

    List<ForumPost> getAllForumPostsByParentForumPostId(Long parentForumPostId);

    List<ForumPost> getAllForumPostsOfAForumThread(Long forumThreadId) throws ForumThreadNotFoundException;

    ForumPost updateForumPost(ForumPost updatedForumPost) throws ForumPostNotFoundException, InputDataValidationException;

    Boolean deleteForumPost(Long forumPostId) throws ForumPostNotFoundException, DeleteForumPostException;

    Boolean deleteForumPostAndDisassociateFromForumThread(Long forumPostId) throws ForumPostNotFoundException, DeleteForumPostException, UpdateForumThreadException, ForumThreadNotFoundException;
}
