package com.spring.kodo.controller;

import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.ForumCategory;
import com.spring.kodo.entity.ForumPost;
import com.spring.kodo.entity.ForumThread;
import com.spring.kodo.entity.rest.request.AddForumPostToForumThreadReq;
import com.spring.kodo.entity.rest.request.CreateNewForumThreadReq;
import com.spring.kodo.entity.rest.request.UpdateForumThreadReq;
import com.spring.kodo.service.inter.CourseService;
import com.spring.kodo.service.inter.ForumCategoryService;
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
@RequestMapping(path = "/forumThread")
public class ForumThreadController
{

    Logger logger = LoggerFactory.getLogger(ForumThreadController.class);

    @Autowired
    private ForumThreadService forumThreadService;

    @Autowired
    private ForumCategoryService forumCategoryService;

    @Autowired
    private CourseService courseService;

    @PostMapping("/createNewForumThread")
    public ForumThread createNewForumThread(
            @RequestPart(name = "forumThread", required = true) CreateNewForumThreadReq createNewForumThreadReq
    )
    {
        if (createNewForumThreadReq != null)
        {
            logger.info("HIT forumThread/createNewForumThread | POST | Received : " + createNewForumThreadReq);
            try
            {
                ForumThread newForumThead = new ForumThread(createNewForumThreadReq.getName(), createNewForumThreadReq.getDescription(), createNewForumThreadReq.getTimeStamp());
                newForumThead = this.forumThreadService.createNewForumThread(newForumThead, createNewForumThreadReq.getAccountId());
                ForumCategory forumCategory = this.forumCategoryService.getForumCategoryByForumCategoryId(createNewForumThreadReq.getForumCategoryId());
                this.forumCategoryService.addForumThreadToForumCategory(forumCategory, newForumThead);
                return newForumThead;
            }
            catch (AccountNotFoundException | ForumCategoryNotFoundException | UpdateForumCategoryException | ForumThreadNotFoundException ex)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
            catch (InputDataValidationException ex)
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Create New Forum Thread Request");
        }
    }

    @GetMapping("/getForumThreadByForumThreadId/{forumThreadId}")
    public ForumThread getForumThreadByForumThreadId(@PathVariable Long forumThreadId)
    {
        try
        {
            ForumThread forumThread = this.forumThreadService.getForumThreadByForumThreadId(forumThreadId);

            forumThread.getAccount().setCourses(null);
            forumThread.getAccount().setEnrolledCourses(null);
            forumThread.getAccount().setInterests(null);

            for (ForumPost forumPost : forumThread.getForumPosts())
            {
                forumPost.getAccount().setCourses(null);
                forumPost.getAccount().setEnrolledCourses(null);
                forumPost.getAccount().setInterests(null);
                forumPost.setParentForumPost(null);
            }

            return forumThread;
        }
        catch (ForumThreadNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getForumThreadByForumThreadIdAndCourseId/{forumThreadId}/{courseId}")
    public ForumThread getForumThreadByForumThreadIdAndCourseId(@PathVariable Long forumThreadId, @PathVariable Long courseId)
    {
        try
        {
            Course course = this.courseService.getCourseByForumThreadId(forumThreadId);

            if (course.getCourseId().equals(courseId))
            {
                ForumThread forumThread = this.forumThreadService.getForumThreadByForumThreadId(forumThreadId);

                forumThread.getAccount().setCourses(null);
                forumThread.getAccount().setEnrolledCourses(null);
                forumThread.getAccount().setInterests(null);

                for (ForumPost forumPost : forumThread.getForumPosts())
                {
                    forumPost.getAccount().setCourses(null);
                    forumPost.getAccount().setEnrolledCourses(null);
                    forumPost.getAccount().setInterests(null);
                    forumPost.setParentForumPost(null);
                }

                return forumThread;
            }
            else
            {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this forum thread");
            }
        }
        catch (CourseNotFoundException | ForumThreadNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getForumThreadByName/{name}")
    public ForumThread getForumThreadByName(@PathVariable String name)
    {
        try
        {
            return this.forumThreadService.getForumThreadByName(name);
        }
        catch (ForumThreadNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllForumThreads")
    public List<ForumThread> getAllForumThreads()
    {
        return this.forumThreadService.getAllForumThreads();
    }

    @GetMapping("/getAllForumThreadsByForumCategoryId/{forumCategoryId}")
    public List<ForumThread> getAllForumThreadsByForumCategoryId(@PathVariable Long forumCategoryId)
    {
        try
        {
            forumCategoryService.getForumCategoryByForumCategoryId(forumCategoryId);

            List<ForumThread> forumThreads = forumThreadService.getAllForumThreadsByForumCategoryId(forumCategoryId);
            for (ForumThread forumThread : forumThreads)
            {
                forumThread.getAccount().setCourses(null);
                forumThread.getAccount().setEnrolledCourses(null);
                forumThread.getAccount().setInterests(null);

                for (ForumPost forumPost : forumThread.getForumPosts())
                {
                    forumPost.setAccount(null);
                    forumPost.setParentForumPost(null);
                }
            }

            return forumThreads;
        }
        catch (ForumCategoryNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PutMapping("/updateForumThread")
    public ForumThread updateForumThread(
            @RequestPart(name = "forumThread", required = true) UpdateForumThreadReq updateForumThreadReq
    )
    {
        if (updateForumThreadReq != null)
        {
            try
            {
                ForumThread updatedForumThread = this.forumThreadService.updateForumThread(updateForumThreadReq.getForumThread());
                return updatedForumThread;
            }
            catch (ForumThreadNotFoundException ex)
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Update Forum Thread Request");
        }
    }

    @DeleteMapping("/deleteForumThread/{forumThreadId}")
    public ResponseEntity<String> deleteForumThread(@PathVariable Long forumThreadId)
    {
        try
        {
            this.forumThreadService.deleteForumThreadAndDisassociateFromForumCategory(forumThreadId);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted forum thread with ID: " + forumThreadId);
        }
        catch (ForumCategoryNotFoundException | ForumThreadNotFoundException | ForumPostNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        catch (DeleteForumThreadException | DeleteForumPostException | UpdateForumCategoryException ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PutMapping("/addForumPostToForumThread")
    public ForumThread addForumPostToForumThread(
            @RequestPart(name = "forumThreadAndForumPost", required = true) AddForumPostToForumThreadReq addForumPostToForumThreadReq
    )
    {
        if (addForumPostToForumThreadReq != null)
        {
            try
            {
                ForumThread forumThreadWithPost = this.forumThreadService.addForumPostToForumThread(addForumPostToForumThreadReq.getForumThread(), addForumPostToForumThreadReq.getForumPost());
                return forumThreadWithPost;
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
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Add Forum Post to Forum Thread Request");
        }
    }
}


