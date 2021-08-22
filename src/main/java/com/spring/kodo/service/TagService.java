package com.spring.kodo.service;

import com.spring.kodo.entity.Tag;
import com.spring.kodo.util.exception.TagNotFoundException;

import java.util.List;

public interface TagService
{
    Tag createNewTag(Tag tag);
    List<Tag> createNewTags(List<Tag> tag);

    Tag getTagByTagId(Long tagId) throws TagNotFoundException;
    List<Tag> getAllTags();
}