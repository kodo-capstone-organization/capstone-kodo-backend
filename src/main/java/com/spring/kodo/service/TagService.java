package com.spring.kodo.service;

import com.spring.kodo.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService
{
    Optional<Tag> getTagByTagId(Long tagId);
    List<Tag> getTags();
}