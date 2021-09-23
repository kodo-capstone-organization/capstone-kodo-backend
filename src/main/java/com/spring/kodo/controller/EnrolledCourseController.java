package com.spring.kodo.controller;

import com.spring.kodo.entity.EnrolledCourse;
import com.spring.kodo.restentity.response.EnrolledCourseWithStudentCompletion;
import com.spring.kodo.restentity.response.EnrolledCourseWithStudentResp;
import com.spring.kodo.service.inter.*;
import com.spring.kodo.util.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = "/enrolledCourse")
public class EnrolledCourseController
{
    Logger logger = LoggerFactory.getLogger(EnrolledCourseController.class);

    @Autowired
    private EnrolledCourseService enrolledCourseService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private EnrolledLessonService enrolledLessonService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/getEnrolledCourseByEnrolledCourseId/{enrolledCourseId}")
    public EnrolledCourse getEnrolledCourseByEnrolledCourseId(@PathVariable Long enrolledCourseId)
    {
        try
        {
            EnrolledCourse enrolledCourse = enrolledCourseService.getEnrolledCourseByEnrolledCourseId(enrolledCourseId);

            return enrolledCourse;
        }
        catch (EnrolledCourseNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getEnrolledCourseByStudentIdAndCourseId/{studentId}/{courseId}")
    public EnrolledCourse getEnrolledCourseByStudentIdAndCourseId(@PathVariable Long studentId, @PathVariable Long courseId)
    {
        try
        {
            EnrolledCourse enrolledCourse = enrolledCourseService.getEnrolledCourseByStudentIdAndCourseId(studentId, courseId);

            return enrolledCourse;
        }
        catch (EnrolledCourseNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllEnrolledCourses")
    public List<EnrolledCourse> getAllEnrolledCourses()
    {
        return this.enrolledCourseService.getAllEnrolledCourses();
    }

    @GetMapping("/setCourseRatingByEnrolledCourseId/{enrolledCourseId}/{courseRating}")
    public EnrolledCourse setCourseRatingByEnrolledCourseId(@PathVariable Long enrolledCourseId, @PathVariable Integer courseRating)
    {
        try
        {
            EnrolledCourse enrolledCourse = enrolledCourseService.setCourseRatingByEnrolledCourseId(enrolledCourseId, courseRating);

            return enrolledCourse;
        }
        catch (EnrolledCourseNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        catch (InputDataValidationException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, ex.getMessage());
        }
    }

    @GetMapping("/getEnrolledCoursesWithStudentCompletion/{courseId}")
    public List<EnrolledCourseWithStudentResp> getEnrolledCoursesWithStudentCompletion(@PathVariable Long courseId)
    {
        return this.enrolledCourseService.getAllCompletionPercentagesByCourseId(courseId);
    }

    @GetMapping("/getEnrolledCoursesWithStudents/{courseId}")
    public List<EnrolledCourseWithStudentCompletion> getEnrolledCoursesWithStudents(@PathVariable Long courseId)
    {
        return this.enrolledCourseService.getAllEnrolledStudentsCompletion(courseId);
    }
}
