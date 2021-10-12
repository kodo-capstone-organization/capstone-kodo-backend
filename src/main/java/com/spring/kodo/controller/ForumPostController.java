package com.spring.kodo.controller;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.ForumPost;
import com.spring.kodo.entity.ForumThread;
import com.spring.kodo.restentity.request.CreateNewForumPostReplyReq;
import com.spring.kodo.restentity.request.CreateNewForumPostReq;
import com.spring.kodo.restentity.request.UpdateForumPostReq;
import com.spring.kodo.restentity.response.ForumPostWithRepliesResp;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/forumPost")
public class ForumPostController
{

    Logger logger = LoggerFactory.getLogger(ForumPostController.class);

    @Autowired
    private ForumPostService forumPostService;

    @Autowired
    private ForumThreadService forumThreadService;


    @PostMapping("/createNewForumPost")
    public ForumPost createNewForumPost(
            @RequestPart(name = "forumPost", required = true) CreateNewForumPostReq createNewForumPostReq
    )
    {
        if (createNewForumPostReq != null)
        {
            logger.info("HIT forumPost/createNewForumPost | POST | Received : " + createNewForumPostReq);
            try
            {
                ForumPost newForumPost = new ForumPost(createNewForumPostReq.getMessage(), createNewForumPostReq.getTimeStamp());
                newForumPost = this.forumPostService.createNewForumPost(newForumPost, createNewForumPostReq.getAccountId());
                ForumThread forumThread = this.forumThreadService.getForumThreadByForumThreadId(createNewForumPostReq.getForumThreadId());
                this.forumThreadService.addForumPostToForumThread(forumThread, newForumPost);
                return newForumPost;
            }
            catch (AccountNotFoundException | ForumThreadNotFoundException ex)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
            catch (InputDataValidationException | UpdateForumThreadException | ForumPostNotFoundException ex)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }
            catch (UnknownPersistenceException ex)
            {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Create New Forum Post Request");
        }
    }

    @PostMapping("/createNewForumPostReply")
    public ForumPost createNewForumPostReply(
            @RequestPart(name = "forumPostReply", required = true) CreateNewForumPostReplyReq createNewForumPostReplyReq
    )
    {
        if (createNewForumPostReplyReq != null)
        {
            logger.info("HIT forumPost/createNewForumPostReply | POST | Received : " + createNewForumPostReplyReq);
            try
            {
                ForumPost newForumPostReply = createNewForumPostReplyReq.getNewForumPostReply();
                Long accountId = createNewForumPostReplyReq.getAccountId();
                Long parentForumPostId = createNewForumPostReplyReq.getParentForumPostId();

                ForumThread forumThread = forumThreadService.getForumThreadByForumPostId(parentForumPostId);

                newForumPostReply = forumPostService.createNewForumPostReply(newForumPostReply, accountId, parentForumPostId);
                forumThreadService.addForumPostToForumThread(forumThread, newForumPostReply);

                newForumPostReply.setParentForumPost(null);

                return newForumPostReply;
            }
            catch (AccountNotFoundException | ForumPostNotFoundException | ForumThreadNotFoundException ex)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
            catch (UpdateForumThreadException | InputDataValidationException ex)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }
            catch (UnknownPersistenceException ex)
            {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Create New Forum Post Reply Request");
        }
    }

    @GetMapping("/getForumPostByForumPostId/{forumPostId}")
    public ForumPost getForumPostByForumPostId(@PathVariable Long forumPostId)
    {
        try
        {
            return this.forumPostService.getForumPostByForumPostId(forumPostId);
        }
        catch (ForumPostNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllForumPostsByForumThreadId/{forumThreadId}")
    public List<ForumPostWithRepliesResp> getAllForumPostsByForumThreadId(@PathVariable Long forumThreadId)
    {
        List<ForumPost> parentForumPosts = forumPostService.getAllByNullParentPostAndForumThreadId(forumThreadId);

        List<ForumPostWithRepliesResp> forumPostWithRepliesResps = addRepliesToForumPost(parentForumPosts);

        return forumPostWithRepliesResps;
    }

    private List<ForumPostWithRepliesResp> addRepliesToForumPost(List<ForumPost> parentForumPosts)
    {
        Account account;

        List<ForumPostWithRepliesResp> forumPostWithRepliesResps = new ArrayList<>();

        ForumPostWithRepliesResp forumPostWithRepliesResp;

        for (ForumPost parentForumPost : parentForumPosts)
        {
            account = parentForumPost.getAccount();

            account.setInterests(null);
            account.setCourses(null);
            account.setEnrolledCourses(null);

            forumPostWithRepliesResp = new ForumPostWithRepliesResp(
                    parentForumPost.getForumPostId(),
                    parentForumPost.getMessage(),
                    parentForumPost.getTimeStamp(),
                    account
            );

            forumPostWithRepliesResp.setReplies(
                    addRepliesToForumPost(parentForumPost.getReplies())
            );

            forumPostWithRepliesResps.add(forumPostWithRepliesResp);
        }

        return forumPostWithRepliesResps;
    }

    @GetMapping("/getAllForumPosts")
    public List<ForumPost> getAllForumPosts()
    {
        return this.forumPostService.getAllForumPosts();
    }

    @PutMapping("/updateForumPost")
    public ForumPost updateForumPost(
            @RequestPart(name = "forumPost", required = true) UpdateForumPostReq updateForumPostReq
    )
    {
        if (updateForumPostReq != null)
        {
            try
            {
                ForumPost updatedForumPost = this.forumPostService.updateForumPost(updateForumPostReq.getForumPost());
                return updatedForumPost;
            }
            catch (ForumPostNotFoundException ex)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
            catch (InputDataValidationException ex)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Update Forum Post Request");
        }
    }

    @DeleteMapping("/deleteForumPost/{forumPostId}")
    public ResponseEntity<String> deleteForumPost(@PathVariable Long forumPostId) throws DeleteForumPostException
    {
        try
        {
            this.forumPostService.deleteForumPostAndDisassociateFromForumThread(forumPostId);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted forum post with ID: " + forumPostId);
        }
        catch (ForumThreadNotFoundException | ForumPostNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        catch (UpdateForumThreadException ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
