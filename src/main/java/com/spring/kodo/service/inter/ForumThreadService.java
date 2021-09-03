package com.spring.kodo.service.inter;

import com.spring.kodo.entity.ForumThread;
import com.spring.kodo.util.exception.ForumCategoryNotFoundException;
import com.spring.kodo.util.exception.ForumThreadNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;

import java.util.List;

public interface ForumThreadService {

    ForumThread createNewForumThread (ForumThread newForumThread) throws InputDataValidationException;

    ForumThread getForumThreadByForumThreadId(Long forumThreadId) throws ForumThreadNotFoundException;

    ForumThread getForumThreadByName(String name) throws ForumThreadNotFoundException;

    List<ForumThread> getAllForumThreadsOfAForumCategory(Long forumCategoryId) throws ForumCategoryNotFoundException;

    ForumThread updateForumThread(Long forumThreadId, ForumThread updatedForumThread) throws ForumThreadNotFoundException;

    Boolean deleteForumThread(Long forumThreadId) throws ForumThreadNotFoundException;

}
