package com.spring.kodo.controller;

import com.spring.kodo.restentity.request.GetNumberOfStudentAttemptsLeftReq;
import com.spring.kodo.service.inter.StudentAttemptService;
import com.spring.kodo.util.exception.AccountNotFoundException;
import com.spring.kodo.util.exception.QuizNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/studentAttempt")
public class StudentAttemptController {
    Logger logger = LoggerFactory.getLogger(LessonController.class);

    @Autowired
    private StudentAttemptService studentAttemptService;

    @GetMapping("/getNumberOfStudentAttemptsLeft")
    public Integer getNumberOfStudentAttemptsLeft(@RequestPart(name = "accountAndQuizId", required = true) GetNumberOfStudentAttemptsLeftReq getNumberOfStudentAttemptsLeftReq) {
        if (getNumberOfStudentAttemptsLeftReq != null) {
            try {
                Integer numberOfStudentAttemptsLeft = studentAttemptService.getNumberOfStudentAttemptsLeft(getNumberOfStudentAttemptsLeftReq.getAccountId(), getNumberOfStudentAttemptsLeftReq.getQuizId());
                return numberOfStudentAttemptsLeft;
            } catch (AccountNotFoundException | QuizNotFoundException ex) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Get Number of Student Attempts Request");
        }
    }
}