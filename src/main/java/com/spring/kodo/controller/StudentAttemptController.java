package com.spring.kodo.controller;

import com.spring.kodo.service.inter.StudentAttemptService;
import com.spring.kodo.util.exception.AccountNotFoundException;
import com.spring.kodo.util.exception.QuizNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/studentAttempt")
public class StudentAttemptController {
    Logger logger = LoggerFactory.getLogger(LessonController.class);

    @Autowired
    private StudentAttemptService studentAttemptService;
}