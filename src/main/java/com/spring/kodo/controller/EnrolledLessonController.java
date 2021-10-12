package com.spring.kodo.controller;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.EnrolledLesson;
import com.spring.kodo.restentity.response.EnrolledLessonWithStudentNameResp;
import com.spring.kodo.service.inter.AccountService;
import com.spring.kodo.service.inter.EnrolledLessonService;
import com.spring.kodo.util.exception.AccountNotFoundException;
import com.spring.kodo.util.exception.EnrolledLessonNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/enrolledLesson")
public class EnrolledLessonController
{
    Logger logger = LoggerFactory.getLogger(EnrolledLessonController.class);

    @Autowired
    private EnrolledLessonService enrolledLessonService;

    @Autowired
    private AccountService accountService;


    @GetMapping("/getEnrolledLessonByEnrolledLessonId/{enrolledLessonId}")
    public EnrolledLesson getEnrolledLessonByEnrolledLessonId(@PathVariable Long enrolledLessonId)
    {
        try
        {
            return this.enrolledLessonService.getEnrolledLessonByEnrolledLessonId(enrolledLessonId);
        }
        catch (EnrolledLessonNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getEnrolledLessonByEnrolledLessonIdAndAccountId/{enrolledLessonId}/{accountId}")
    public EnrolledLesson getEnrolledLessonByEnrolledLessonIdAndAccountId(@PathVariable Long enrolledLessonId, @PathVariable Long accountId)
    {
        try
        {
            Account account = this.accountService.getAccountByEnrolledLessonId(enrolledLessonId);

            if (account.getAccountId().equals(accountId))
            {
                EnrolledLesson enrolledLesson = enrolledLessonService.getEnrolledLessonByEnrolledLessonId(enrolledLessonId);

                return enrolledLesson;
            }
            else
            {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this lesson");
            }
        }
        catch (AccountNotFoundException | EnrolledLessonNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getEnrolledLessonByStudentIdAndLessonId/{studentId}/{lessonId}")
    public EnrolledLesson getEnrolledLessonByStudentIdAndLessonId(@PathVariable Long studentId, @PathVariable Long lessonId)
    {
        try
        {
            return this.enrolledLessonService.getEnrolledLessonByStudentIdAndLessonId(studentId, lessonId);
        }
        catch (EnrolledLessonNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllEnrolledLessons")
    public List<EnrolledLesson> getAllEnrolledLessons()
    {
        return this.enrolledLessonService.getAllEnrolledLessons();
    }

    @GetMapping("/getAllEnrolledLessonsByLessonId/{lessonId}")
    public List<EnrolledLesson> getAllEnrolledLessonsByLessonId(@PathVariable Long lessonId)
    {
        return this.enrolledLessonService.getAllEnrolledLessonsByParentLessonId(lessonId);
    }

    @GetMapping("/getAllEnrolledLessonsWithStudentNameByParentLessonId/{lessonId}")
    public List<EnrolledLessonWithStudentNameResp> getAllEnrolledLessonsWithStudentNameByParentLessonId(@PathVariable Long lessonId)
    {
        try
        {
            List<EnrolledLessonWithStudentNameResp> enrolledLessonsWithStudentNameRespList = new ArrayList<>();

            List<EnrolledLesson> enrolledLessons = this.enrolledLessonService.getAllEnrolledLessonsByParentLessonId(lessonId);

            Account account;
            for (EnrolledLesson enrolledLesson : enrolledLessons)
            {
                account = accountService.getAccountByEnrolledLessonId(enrolledLesson.getEnrolledLessonId());
                enrolledLessonsWithStudentNameRespList.add(
                        new EnrolledLessonWithStudentNameResp(
                                enrolledLesson,
                                account.getName()
                        )
                );
            }

            return enrolledLessonsWithStudentNameRespList;
        }
        catch (AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
