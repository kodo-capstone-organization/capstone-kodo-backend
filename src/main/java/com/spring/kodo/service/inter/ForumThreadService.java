package com.spring.kodo.service.inter;

import com.spring.kodo.entity.ForumPost;
import com.spring.kodo.entity.ForumThread;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface ForumThreadService {

    ForumThread createNewForumThread(ForumThread newForumThread, Long accountId) throws InputDataValidationException, UnknownPersistenceException, AccountNotFoundException;

    ForumThread getForumThreadByForumThreadId(Long forumThreadId) throws ForumThreadNotFoundException;

    ForumThread getForumThreadByName(String name) throws ForumThreadNotFoundException;

    List<ForumThread> getAllForumThreads();

    List<ForumThread> getAllForumThreadsOfAForumCategory(Long forumCategoryId) throws ForumCategoryNotFoundException;

    ForumThread updateForumThread(ForumThread updatedForumThread) throws ForumThreadNotFoundException, InputDataValidationException;

    Boolean deleteForumThread(Long forumThreadId) throws ForumThreadNotFoundException, ForumPostNotFoundException;

    ForumThread addForumPostToForumThread(ForumThread forumThread, ForumPost forumPost) throws UpdateForumThreadException, ForumThreadNotFoundException, ForumPostNotFoundException;
}
