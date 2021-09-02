package com.spring.kodo.controller;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Course;
import com.spring.kodo.restentity.request.CreateNewCourseReq;
import com.spring.kodo.restentity.response.CourseWithTutorResp;
import com.spring.kodo.service.AccountService;
import com.spring.kodo.service.CourseService;
import com.spring.kodo.service.FileService;
import com.spring.kodo.util.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/course")
public class CourseController
{
    Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private FileService fileService;

    @GetMapping("/getAllCourses")
    public List<CourseWithTutorResp> getAllCourses()
    {
        try
        {
            List<CourseWithTutorResp> courseWithTutorResps = new ArrayList<>();

            List<Course> courses = this.courseService.getAllCourses();

            Long courseId;
            Account tutor;
            CourseWithTutorResp courseWithTutorResp;

            for (Course course : courses)
            {
                courseId = course.getCourseId();
                tutor = this.accountService.getAccountByCourseId(courseId);

                courseWithTutorResp = new CourseWithTutorResp(
                        course.getName(),
                        course.getDescription(),
                        course.getPrice(),
                        course.getBannerUrl(),
                        course.getEnrollment(),
                        course.getCourseTags(),
                        tutor
                );

                courseWithTutorResps.add(courseWithTutorResp);
            }

            return courseWithTutorResps;
        }
        catch (AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getCourseByCourseId/{courseId}")
    public CourseWithTutorResp getCourseByCourseId(@PathVariable Long courseId)
    {
        try
        {
            Course course = this.courseService.getCourseByCourseId(courseId);
            Account tutor = this.accountService.getAccountByCourseId(courseId);

            CourseWithTutorResp courseWithTutorResp = new CourseWithTutorResp(
                    course.getName(),
                    course.getDescription(),
                    course.getPrice(),
                    course.getBannerUrl(),
                    course.getEnrollment(),
                    course.getCourseTags(),
                    tutor
            );

            return courseWithTutorResp;
        }
        catch (CourseNotFoundException | AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/searchCourseByKeyword/{keyword}")
    public List<Course> searchCourseByKeyword(@PathVariable String keyword)
    {
        try
        {
            return this.courseService.getAllCoursesByKeyword(keyword);
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
