package com.spring.kodo.service.inter;

import com.spring.kodo.entity.ForumPost;
import com.spring.kodo.entity.ForumThread;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface ForumThreadService
{

    ForumThread createNewForumThread(ForumThread newForumThread, Long accountId) throws InputDataValidationException, UnknownPersistenceException, AccountNotFoundException;

    ForumThread getForumThreadByForumThreadId(Long forumThreadId) throws ForumThreadNotFoundException;

    ForumThread getForumThreadByName(String name) throws ForumThreadNotFoundException;

    ForumThread getForumThreadByForumPostId(Long forumPostId) throws ForumThreadNotFoundException;

    List<ForumThread> getAllForumThreads();

    List<ForumThread> getAllForumThreadsByForumCategoryId(Long forumCategoryId) throws ForumCategoryNotFoundException;

    ForumThread updateForumThread(ForumThread updatedForumThread) throws ForumThreadNotFoundException, InputDataValidationException;

    Boolean deleteForumThread(Long forumThreadId) throws ForumThreadNotFoundException, ForumPostNotFoundException, DeleteForumPostException, DeleteForumThreadException;

    Boolean deleteForumThreadAndDisassociateFromForumCategory(Long forumThreadId) throws DeleteForumThreadException, ForumThreadNotFoundException, DeleteForumPostException, ForumPostNotFoundException, ForumCategoryNotFoundException, UpdateForumCategoryException;

    ForumThread addForumPostToForumThread(ForumThread forumThread, ForumPost forumPost) throws UpdateForumThreadException, ForumThreadNotFoundException, ForumPostNotFoundException;

    ForumThread removeForumPostToForumThreadByForumPostId(Long forumPostId) throws UpdateForumThreadException, ForumThreadNotFoundException;
}
