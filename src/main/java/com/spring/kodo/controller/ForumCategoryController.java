package com.spring.kodo.controller;

import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.ForumCategory;
import com.spring.kodo.entity.ForumPost;
import com.spring.kodo.entity.ForumThread;
import com.spring.kodo.entity.rest.request.AddForumThreadToForumCategoryReq;
import com.spring.kodo.entity.rest.request.CreateNewForumCategoryReq;
import com.spring.kodo.entity.rest.request.UpdateForumCategoryReq;
import com.spring.kodo.service.inter.CourseService;
import com.spring.kodo.service.inter.ForumCategoryService;
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
@RequestMapping(path = "/forumCategory")
public class ForumCategoryController
{

    Logger logger = LoggerFactory.getLogger(ForumCategoryController.class);

    @Autowired
    private ForumCategoryService forumCategoryService;

    @Autowired
    private CourseService courseService;

    //    ForumCategory createNewForumCategory(ForumCategory newForumCategory, Long courseId) throws InputDataValidationException, UnknownPersistenceException, CourseNotFoundException;

    @PostMapping("/createNewForumCategory")
    public ForumCategory createNewForumCategory(
            @RequestPart(name = "forumCategory", required = true) CreateNewForumCategoryReq createNewForumCategoryReq
    )
    {
        if (createNewForumCategoryReq != null)
        {
            logger.info("HIT forumCategory/createNewForumCategory | POST | Received : " + createNewForumCategoryReq);
            try
            {
                ForumCategory newForumCategory = new ForumCategory(createNewForumCategoryReq.getName(), createNewForumCategoryReq.getDescription());
                newForumCategory = this.forumCategoryService.createNewForumCategory(newForumCategory, createNewForumCategoryReq.getCourseId());
                return newForumCategory;
            }
            catch (CourseNotFoundException ex)
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

    @GetMapping("/getForumCategoryByForumCategoryId/{forumCategoryId}")
    public ForumCategory getForumCategoryByForumCategoryId(@PathVariable Long forumCategoryId)
    {
        try
        {
            ForumCategory forumCategory = this.forumCategoryService.getForumCategoryByForumCategoryId(forumCategoryId);

            for (ForumThread forumThread : forumCategory.getForumThreads())
            {
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
            }

            forumCategory.setCourse(null);

            return forumCategory;
        }
        catch (ForumCategoryNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getForumCategoryByForumCategoryIdAndCourseId/{forumCategoryId}/{courseId}")
    public ForumCategory getForumCategoryByForumCategoryIdAndCourseId(@PathVariable Long forumCategoryId, @PathVariable Long courseId)
    {
        try
        {
            Course course = this.courseService.getCourseByForumCategoryId(forumCategoryId);

            if (course.getCourseId().equals(courseId))
            {
                ForumCategory forumCategory = this.forumCategoryService.getForumCategoryByForumCategoryId(forumCategoryId);

                for (ForumThread forumThread : forumCategory.getForumThreads())
                {
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
                }

                forumCategory.setCourse(null);

                return forumCategory;
            }
            else
            {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this forum");
            }
        }
        catch (CourseNotFoundException | ForumCategoryNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getForumCategoryWithForumThreadsOnlyByForumCategoryId/{forumCategoryId}")
    public ForumCategory getForumCategoryWithForumThreadsOnlyByForumCategoryId(@PathVariable Long forumCategoryId)
    {
        try
        {
            ForumCategory forumCategory = this.forumCategoryService.getForumCategoryByForumCategoryId(forumCategoryId);

            for (ForumThread forumThread : forumCategory.getForumThreads())
            {
                forumThread.setForumPosts(null);
                forumThread.setAccount(null);
            }
            forumCategory.setCourse(null);

            return forumCategory;
        }
        catch (ForumCategoryNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getForumCategoryByName/{name}")
    public ForumCategory getForumCategoryByName(@PathVariable String name)
    {
        try
        {
            return this.forumCategoryService.getForumCategoryByName(name);
        }
        catch (ForumCategoryNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllForumCategories")
    public List<ForumCategory> getAllForumCategories()
    {
        return this.forumCategoryService.getAllForumCategories();
    }

    @GetMapping("/getAllForumCategoriesWithoutForumThreads")
    public List<ForumCategory> getAllForumCategoriesWithoutForumThreads()
    {
        List<ForumCategory> forumCategories = this.forumCategoryService.getAllForumCategories();

        for (ForumCategory forumCategory : forumCategories)
        {
            forumCategory.setForumThreads(null);
        }

        return forumCategories;
    }

    @GetMapping("/getAllForumCategoriesByCourseId/{courseId}")
    public List<ForumCategory> getAllForumCategoriesByCourseId(@PathVariable Long courseId)
    {
        try
        {
            return this.forumCategoryService.getAllForumCategoriesByCourseId(courseId);
        }
        catch (ForumCategoryNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllForumCategoriesWithForumThreadsOnlyByCourseId/{courseId}")
    public List<ForumCategory> getAllForumCategoriesWithForumThreadsOnlyByCourseId(@PathVariable Long courseId)
    {
        try
        {
            List<ForumCategory> forumCategories = this.forumCategoryService.getAllForumCategoriesByCourseId(courseId);

            for (ForumCategory forumCategory : forumCategories)
            {
                for (ForumThread forumThread : forumCategory.getForumThreads())
                {
                    forumThread.setForumPosts(null);
                    forumThread.setAccount(null);
                }
                forumCategory.setCourse(null);
            }

            return forumCategories;
        }
        catch (ForumCategoryNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    //ForumCategory updateForumCategory(ForumCategory updatedForumCategory) throws ForumCategoryNotFoundException, InputDataValidationException;

    @PutMapping("/updateForumCategory")
    public ForumCategory updateForumCategory(
            @RequestPart(name = "forumCategory", required = true) UpdateForumCategoryReq updateForumCategoryReq
    )
    {
        if (updateForumCategoryReq != null)
        {
            try
            {
                ForumCategory updatedForumCategory = this.forumCategoryService.updateForumCategory(updateForumCategoryReq.getForumCategory());
                return updatedForumCategory;
            }
            catch (ForumCategoryNotFoundException ex)
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Update Forum Category Request");
        }
    }

    @DeleteMapping("/deleteForumCategory/{forumCategoryId}")
    public ResponseEntity<String> deleteForumCategory(@PathVariable Long forumCategoryId)
    {
        try
        {
            this.forumCategoryService.deleteForumCategory(forumCategoryId);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted forum category with ID: " + forumCategoryId);
        }
        catch (ForumCategoryNotFoundException | ForumThreadNotFoundException | ForumPostNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        catch (DeleteForumCategoryException | DeleteForumThreadException | DeleteForumPostException ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PutMapping("/addForumThreadToForumCategory")
    public ForumCategory addForumThreadToForumCategory(
            @RequestPart(name = "forumCategoryAndForumThread", required = true) AddForumThreadToForumCategoryReq addForumThreadToForumCategoryReq
    )
    {
        if (addForumThreadToForumCategoryReq != null)
        {
            try
            {
                ForumCategory forumCategoryWithThread = this.forumCategoryService.addForumThreadToForumCategory(addForumThreadToForumCategoryReq.getForumCategory(), addForumThreadToForumCategoryReq.getForumThread());
                return forumCategoryWithThread;
            }
            catch (ForumThreadNotFoundException | ForumCategoryNotFoundException ex)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
            catch (UpdateForumCategoryException ex)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Add Forum Thread to Forum Category Request");
        }
    }
}
