package com.spring.kodo.controller;

import com.spring.kodo.entity.Tag;
import com.spring.kodo.util.exception.TagNotFoundException;
import com.spring.kodo.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = "/tag")
public class TagController
{
    @Autowired
    private TagService tagService;

    @GetMapping("/getTagByTagId/{tagId}")
    public Tag getTagByTagId(@PathVariable Long tagId)
    {
        try
        {
            Tag tag = this.tagService.getTagByTagId(tagId);
            return tag;
        }
        catch (TagNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllTags")
    public List<Tag> getAllTags()
    {
        return this.tagService.getAllTags();
    }
}