package com.spring.kodo.controller;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Tag;
import com.spring.kodo.restentity.request.CreateNewTagsReq;
import com.spring.kodo.restentity.response.TagWithAccountsCountAndCoursesCount;
import com.spring.kodo.util.exception.*;
import com.spring.kodo.service.inter.TagService;
import org.aspectj.bridge.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = "/tag")
public class TagController
{
    Logger logger = LoggerFactory.getLogger(TagController.class);
    @Autowired
    private TagService tagService;

    @GetMapping("/getTagByTagId/{tagId}")
    public Tag getTagByTagId(@PathVariable Long tagId)
    {
        try
        {
            Tag tag = tagService.getTagByTagId(tagId);
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
        return tagService.getAllTags();
    }

    @GetMapping("/getAllTagsWithAccountsCountAndCoursesCount")
    public List<TagWithAccountsCountAndCoursesCount> getAllTagsWithAccountsCountAndCoursesCount()
    {
        return tagService.getAllTagsWithAccountsCountAndCoursesCount();
    }

    @GetMapping("/deleteTagByTagId/{tagId}")
    public Tag deleteTagByTagId(@PathVariable Long tagId)
    {
        try
        {
            Tag tag = tagService.deleteTagByTagId(tagId);

            return tag;
        }
        catch (TagNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        catch (DeleteTagException ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PostMapping("/createNewTags")
    public List<String> createNewTags(@RequestPart(name="tags", required = true) CreateNewTagsReq createNewTagsReq) {
        if (createNewTagsReq != null) {
            logger.info("HIT account/createNewTags | POST | Received : " + createNewTagsReq);
            try {
                List<String> newTags = createNewTagsReq.getTags();
                for (String t: newTags) {
                    tagService.getTagByTitleOrCreateNew(t);
                }
                return newTags;

            } catch ( TagNameExistsException | InputDataValidationException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            } catch ( UnknownPersistenceException ex ) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Create New Tags Request");
        }
    }
}
