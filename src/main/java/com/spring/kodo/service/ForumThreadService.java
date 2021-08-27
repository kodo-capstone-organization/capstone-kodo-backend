package com.spring.kodo.service;

import com.spring.kodo.entity.ForumThread;
import com.spring.kodo.util.exception.ForumThreadNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;

public interface ForumThreadService {

    ForumThread createNewForumThread (ForumThread newForumThread) throws InputDataValidationException;

    ForumThread getForumThreadByForumThreadId(Long forumThreadId) throws ForumThreadNotFoundException;

    ForumThread getForumThreadByName(String name) throws ForumThreadNotFoundException;

    ForumThread updateForumThread(Long forumThreadId, ForumThread updatedForumThread) throws ForumThreadNotFoundException;

    Boolean deleteForumThread(Long forumThreadId) throws ForumThreadNotFoundException;

}
