package com.spring.kodo.controller;

import com.spring.kodo.entity.Course;
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

    
  
}
