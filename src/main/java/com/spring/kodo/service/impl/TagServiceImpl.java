package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Tag;
import com.spring.kodo.util.exception.TagNotFoundException;
import com.spring.kodo.repository.TagRepository;
import com.spring.kodo.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService
{
    @Autowired // With this annotation, we do not to populate TagRepository in this class' constructor
    private TagRepository tagRepository;

    @Override
    public Tag createNewTag(Tag tag)
    {
        return tagRepository.save(tag);
    }

    @Override
    public List<Tag> createNewTags(List<Tag> tags)
    {
        return tagRepository.saveAll(tags);
    }

    @Override
    public Tag getTagByTagId(Long tagId) throws TagNotFoundException
    {
        Tag tag = tagRepository.findById(tagId).orElse(null);

        if (tag != null)
        {
            return tag;
        }
        else
        {
            throw new TagNotFoundException("Tag with ID: " + tagId + " does not exist!");
        }
    }

    @Override
    public List<Tag> getAllTags()
    {
        return tagRepository.findAll();
    }
}

