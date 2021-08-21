package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Tag;
import com.spring.kodo.repository.TagRepository;
import com.spring.kodo.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService
{
    @Autowired // With this annotation, we do not to populate TagRepository in this class' constructor
    private TagRepository tagRepository;

    @Override
    public Optional<Tag> getTagByTagId(Long tagId)
    {
        return this.tagRepository.findById(tagId);
    }

    @Override
    public List<Tag> getTags()
    {
        return this.tagRepository.findAll();
    }
}

