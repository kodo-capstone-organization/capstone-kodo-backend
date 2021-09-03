package com.spring.kodo.controller;

import com.spring.kodo.entity.Lesson;
import com.spring.kodo.service.inter.FileService;
import com.spring.kodo.service.inter.LessonService;
import com.spring.kodo.util.exception.LessonNotFoundException;
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
@RequestMapping(path = "/lesson")
public class LessonController
{
    Logger logger = LoggerFactory.getLogger(LessonController.class);

    @Autowired
    private LessonService lessonService;

    @Autowired
    private FileService fileService;

    @GetMapping("/getAllLessons")
    public List<Lesson> getAllLessons()
    {
        return this.lessonService.getAllLessons();
    }

    @GetMapping("/getLessonByLessonId/{lessonId}")
    public Lesson getLessonByLessonId(@PathVariable Long lessonId)
    {
        try
        {
            return this.lessonService.getLessonByLessonId(lessonId);
        }
        catch (LessonNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}