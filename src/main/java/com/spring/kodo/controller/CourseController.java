package com.spring.kodo.controller;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Course;
import com.spring.kodo.restentity.CreateNewAccountReq;
import com.spring.kodo.restentity.CreateNewCourseReq;
import com.spring.kodo.service.CourseService;
import com.spring.kodo.service.FileService;
import com.spring.kodo.util.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = "/course")
public class CourseController
{
    Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private FileService fileService;

    @GetMapping("/getAllCourses")
    public List<Course> getAllCourses()
    {
        return this.courseService.getAllCourses();
    }

    @GetMapping("/getCourseByCourseId/{courseId}")
    public Course getCourseByCourseId(@PathVariable Long courseId)
    {
        try
        {
           return this.courseService.getCourseByCourseId(courseId);
        }
        catch (CourseNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/searchCourseByKeyword/{keyword}")
    public List<Course> searchCourseByKeyword(@PathVariable String keyword) {

        try
        {
            return this.courseService.searchCourseByKeyword(keyword);
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }

    }

    @PostMapping("/createNewCourse")
    public Course createNewCourse(
            @RequestPart(name = "course", required = true) CreateNewCourseReq createNewCourseReq
    )
    {
        if (createNewCourseReq != null)
        {
            logger.info("HIT account/createNewCourse | POST | Received : " + createNewCourseReq);
            try
            {
                Course newCourse = new Course(createNewCourseReq.getName(), createNewCourseReq.getDescription(), createNewCourseReq.getPrice(), createNewCourseReq.getBannerUrl());
                newCourse = this.courseService.createNewCourse(newCourse, createNewCourseReq.getTutorId(), createNewCourseReq.getTagTitles());

                return newCourse;
            }
            catch (InputDataValidationException | TagNameExistsException | CreateNewCourseException | UpdateCourseException ex)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }
            catch (TagNotFoundException | AccountNotFoundException | CourseNotFoundException ex)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
            catch (UnknownPersistenceException ex)
            {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Create New Course Request");
        }
    }
}
