package com.spring.kodo.controller;

import com.spring.kodo.entity.Quiz;
import com.spring.kodo.entity.StudentAttempt;
import com.spring.kodo.service.inter.StudentAttemptService;
import com.spring.kodo.util.exception.StudentAttemptNotFoundException;
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
@RequestMapping(path = "/studentAttempt")
public class StudentAttemptController
{
    Logger logger = LoggerFactory.getLogger(LessonController.class);

    @Autowired
    private StudentAttemptService studentAttemptService;

    @GetMapping("/getStudentAttemptByStudentAttemptId/{studentAttemptId}")
    public StudentAttempt getStudentAttemptByStudentAttemptId(@PathVariable Long studentAttemptId)
    {
        try
        {
            return this.studentAttemptService.getStudentAttemptByStudentAttemptId(studentAttemptId);
        }
        catch (StudentAttemptNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllStudentAttempts")
    public List<StudentAttempt> getAllStudentAttempts()
    {
        return this.studentAttemptService.getAllStudentAttempts();
    }
}