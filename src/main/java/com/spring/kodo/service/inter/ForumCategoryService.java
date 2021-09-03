package com.spring.kodo.service.inter;

import com.spring.kodo.entity.ForumCategory;
import com.spring.kodo.entity.ForumThread;
import com.spring.kodo.util.exception.*;

public interface ForumCategoryService {

    ForumCategory createNewForumCategory (ForumCategory newForumCategory) throws InputDataValidationException, UnknownPersistenceException;

    ForumCategory getForumCategoryByForumCategoryId(Long forumCategoryId) throws ForumCategoryNotFoundException;

    ForumCategory getForumCategoryByName(String name) throws ForumCategoryNotFoundException;

    ForumCategory updateForumCategory(Long forumCategoryId, ForumCategory updatedForumCategory) throws ForumCategoryNotFoundException;

    Boolean deleteForumCategory(Long forumCategoryId) throws ForumCategoryNotFoundException;

    ForumCategory addForumThreadToForumCategory(ForumCategory forumCategory, ForumThread forumThread) throws UpdateForumCategoryException, ForumCategoryNotFoundException, ForumThreadNotFoundException;
}
