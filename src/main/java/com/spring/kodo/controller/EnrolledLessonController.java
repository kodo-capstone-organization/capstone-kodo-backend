package com.spring.kodo.controller;

import com.spring.kodo.entity.*;
import com.spring.kodo.restentity.request.MultimediaReq;
import com.spring.kodo.restentity.request.UpdateLessonReq;
import com.spring.kodo.service.inter.*;
import com.spring.kodo.util.exception.*;
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

    @GetMapping("/getAllEnrolledLessons")
    public List<EnrolledLesson> getAllEnrolledLessons()
    {
        return this.enrolledLessonService.getAllEnrolledLessons();
    }

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
}
