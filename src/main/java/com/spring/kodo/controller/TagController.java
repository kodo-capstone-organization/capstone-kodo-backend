package com.spring.kodo.controller;

import com.spring.kodo.entity.Tag;
import com.spring.kodo.exception.TagNotFoundException;
import com.spring.kodo.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return this.tagService
                .getTagByTagId(tagId)
                .orElseThrow(TagNotFoundException::new);
    }

    @GetMapping("/getAllTags")
    public List<Tag> getAllTags()
    {
        return this.tagService.getTags();
    }
}
