package com.spring.kodo.service.inter;

import com.spring.kodo.entity.Tag;
import com.spring.kodo.restentity.response.TagWithAccountsCountAndCoursesCount;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface TagService
{
    Tag createNewTag(Tag newTag) throws TagNameExistsException, UnknownPersistenceException, InputDataValidationException;

    Tag getTagByTitleOrCreateNew(String tagTitle) throws TagNameExistsException, UnknownPersistenceException, InputDataValidationException;

    Tag getTagByTagId(Long tagId) throws TagNotFoundException;

    Tag getTagByTitle(String tagTitle) throws TagNotFoundException;

    List<Tag> getAllTags();

    List<TagWithAccountsCountAndCoursesCount> getAllTagsWithAccountsCountAndCoursesCount();

    List<Tag> getTopRelevantTagsThroughFrequencyWithLimitByAccountId(Long accountId, Integer limit);

    Tag deleteTagByTagId(Long tagId) throws DeleteTagException, TagNotFoundException;
}