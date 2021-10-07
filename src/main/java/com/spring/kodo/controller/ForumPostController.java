package com.spring.kodo.controller;

import com.spring.kodo.entity.ForumCategory;
import com.spring.kodo.entity.ForumPost;
import com.spring.kodo.entity.ForumThread;
import com.spring.kodo.restentity.request.CreateNewForumPostReq;
import com.spring.kodo.restentity.request.UpdateForumPostReq;
import com.spring.kodo.service.inter.ForumPostService;
import com.spring.kodo.service.inter.ForumThreadService;
import com.spring.kodo.util.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = "/forumPost")
public class ForumPostController {

    Logger logger = LoggerFactory.getLogger(ForumPostController.class);

    @Autowired
    private ForumPostService   forumPostService;

    @Autowired
    private ForumThreadService forumThreadService;


    @PostMapping("/createNewForumPost")
    public ForumPost createNewForumPost(
            @RequestPart(name = "forumPost", required = true) CreateNewForumPostReq createNewForumPostReq
    ) {
        if (createNewForumPostReq != null) {
            logger.info("HIT forumPost/createNewForumPost | POST | Received : " + createNewForumPostReq);
            try {
                ForumPost newForumPost = new ForumPost(createNewForumPostReq.getMessage(), createNewForumPostReq.getTimeStamp());
                newForumPost = this.forumPostService.createNewForumPost(newForumPost, createNewForumPostReq.getAccountId());
                ForumThread forumThread = this.forumThreadService.getForumThreadByForumThreadId(createNewForumPostReq.getForumThreadId());
                this.forumThreadService.addForumPostToForumThread(forumThread, newForumPost);
                return newForumPost;
            } catch (AccountNotFoundException | ForumThreadNotFoundException ex) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            } catch (InputDataValidationException | UpdateForumThreadException | ForumPostNotFoundException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            } catch (UnknownPersistenceException ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Create New Forum Post Request");
        }
    }

    @GetMapping("/getForumPostByForumPostId/{forumPostId}")
    public ForumPost getForumPostByForumPostId(@PathVariable Long forumPostId) {
        try {
            return this.forumPostService.getForumPostByForumPostId(forumPostId);
        } catch (ForumPostNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllForumPostsOfAForumThread/{forumThreadId}")
    public List<ForumPost> getAllForumPostsOfAForumThread(@PathVariable Long forumThreadId) {
        try {
            return this.forumPostService.getAllForumPostsOfAForumThread(forumThreadId);
        } catch (ForumThreadNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllForumPosts")
    public List<ForumPost> getAllForumPosts() {
        return this.forumPostService.getAllForumPosts();
    }

    @PutMapping("/updateForumPost")
    public ForumPost updateForumPost(
            @RequestPart(name = "forumPost", required = true) UpdateForumPostReq updateForumPostReq
    ) {
        if (updateForumPostReq != null) {
            try {
                ForumPost updatedForumPost = this.forumPostService.updateForumPost(updateForumPostReq.getForumPost());
                return updatedForumPost;
            } catch (ForumPostNotFoundException ex) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            } catch (InputDataValidationException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Update Forum Post Request");
        }
    }

    @DeleteMapping("/deleteForumPost/{forumPostId}")
    public ResponseEntity<String> deleteForumPost(@PathVariable Long forumPostId) {
        try {
            Boolean deletedForumPost = this.forumPostService.deleteForumPost(forumPostId);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted forum post with ID: " + forumPostId);
        } catch (ForumPostNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
