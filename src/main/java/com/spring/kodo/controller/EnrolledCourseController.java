package com.spring.kodo.controller;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.EnrolledCourse;
import com.spring.kodo.entity.rest.response.EnrolledCourseWithStudentCompletion;
import com.spring.kodo.entity.rest.response.EnrolledCourseWithStudentResp;
import com.spring.kodo.service.inter.AccountService;
import com.spring.kodo.service.inter.EnrolledCourseService;
import com.spring.kodo.util.exception.AccountNotFoundException;
import com.spring.kodo.util.exception.EnrolledCourseNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/getEnrolledCourseByEnrolledCourseIdAndAccountId/{enrolledCourseId}/{accountId}")
    public EnrolledCourse getEnrolledCourseByEnrolledCourseIdAndAccountId(@PathVariable Long enrolledCourseId, @PathVariable Long accountId)
    {
        try
        {
            Account account = this.accountService.getAccountByEnrolledCourseId(enrolledCourseId);

            if (account.getAccountId().equals(accountId))
            {
                EnrolledCourse enrolledCourse = enrolledCourseService.getEnrolledCourseByEnrolledCourseId(enrolledCourseId);

                return enrolledCourse;
            }
            else
            {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this course");
            }
        }
        catch (AccountNotFoundException | EnrolledCourseNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getEnrolledCourseByCourseIdAndAccountId/{courseId}/{accountId}")
    public EnrolledCourse getEnrolledCourseByCourseIdAndAccountId(@PathVariable Long courseId, @PathVariable Long accountId)
    {
        try
        {
            EnrolledCourse enrolledCourse = enrolledCourseService.getEnrolledCourseByStudentIdAndCourseId(accountId, courseId);

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
