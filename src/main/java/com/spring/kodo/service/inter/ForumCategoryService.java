package com.spring.kodo.service.inter;

import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.ForumCategory;
import com.spring.kodo.entity.ForumThread;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface ForumCategoryService {

    ForumCategory createNewForumCategory(ForumCategory newForumCategory, Long courseId) throws InputDataValidationException, UnknownPersistenceException, CourseNotFoundException;

    ForumCategory getForumCategoryByForumCategoryId(Long forumCategoryId) throws ForumCategoryNotFoundException;

    ForumCategory getForumCategoryByName(String name) throws ForumCategoryNotFoundException;

    List<ForumCategory> getAllForumCategories();

    ForumCategory updateForumCategory(Long forumCategoryId, ForumCategory updatedForumCategory) throws ForumCategoryNotFoundException;

    Boolean deleteForumCategory(Long forumCategoryId) throws ForumCategoryNotFoundException;

    ForumCategory addForumThreadToForumCategory(ForumCategory forumCategory, ForumThread forumThread) throws UpdateForumCategoryException, ForumCategoryNotFoundException, ForumThreadNotFoundException;
}
