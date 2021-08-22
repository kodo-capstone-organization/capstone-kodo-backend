package com.spring.kodo.service;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService
{
    Tag createNewTag(Tag tag);
    List<Tag> createNewTags(List<Tag> tag);

    Optional<Tag> getTagByTagId(Long tagId);
    List<Tag> getTags();
}