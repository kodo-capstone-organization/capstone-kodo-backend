package com.spring.kodo.service.inter;

import com.spring.kodo.entity.ForumCategory;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.ForumCategoryNotFoundException;

public interface ForumCategoryService {

    ForumCategory createNewForumCategory (ForumCategory newForumCategory) throws InputDataValidationException;

    ForumCategory getForumCategoryByForumCategoryId(Long forumCategoryId) throws ForumCategoryNotFoundException;

    ForumCategory getForumCategoryByName(String name) throws ForumCategoryNotFoundException;

    ForumCategory updateForumCategory(Long forumCategoryId, ForumCategory updatedForumCategory) throws ForumCategoryNotFoundException;

    Boolean deleteForumCategory(Long forumCategoryId) throws ForumCategoryNotFoundException;

}
