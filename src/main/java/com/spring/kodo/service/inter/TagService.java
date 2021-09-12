package com.spring.kodo.service.inter;

import com.spring.kodo.entity.Tag;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.TagNameExistsException;
import com.spring.kodo.util.exception.TagNotFoundException;
import com.spring.kodo.util.exception.UnknownPersistenceException;

import java.util.List;

public interface TagService
{
    Tag createNewTag(Tag newTag) throws TagNameExistsException, UnknownPersistenceException, InputDataValidationException;

    Tag getTagByTitleOrCreateNew(String tagTitle) throws TagNameExistsException, UnknownPersistenceException, InputDataValidationException;

    Tag getTagByTagId(Long tagId) throws TagNotFoundException;

    Tag getTagByTitle(String tagTitle) throws TagNotFoundException;

    List<Tag> getAllTags();

    List<Tag> getTopRelevantTagsThroughFrequencyWithLimitByAccountId(Long accountId, Integer limit);
}