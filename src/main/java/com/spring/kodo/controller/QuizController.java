package com.spring.kodo.controller;

import com.spring.kodo.entity.Quiz;
import com.spring.kodo.restentity.response.QuizWithStudentAttemptCountResp;
import com.spring.kodo.service.inter.QuizService;
import com.spring.kodo.util.exception.QuizNotFoundException;
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
@RequestMapping(path = "/quiz")
public class QuizController
{
    Logger logger = LoggerFactory.getLogger(QuizController.class);

    @Autowired
    private QuizService quizService;

    @GetMapping("/getAllQuizzes")
    public List<Quiz> getAllQuizzes()
    {
        return this.quizService.getAllQuizzes();
    }

    @GetMapping("/getQuizByQuizId/{quizId}")
    public Quiz getQuizByQuizId(@PathVariable Long quizId)
    {
        try
        {
            return this.quizService.getQuizByQuizId(quizId);
        }
        catch (QuizNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllQuizzesWithStudentAttemptCountByEnrolledLessonId/{enrolledLessonId}")
    public List<QuizWithStudentAttemptCountResp> getAllQuizzesWithStudentAttemptCountByEnrolledLessonId(@PathVariable Long enrolledLessonId)
    {
        return this.quizService.getAllQuizzesWithStudentAttemptCountByEnrolledLessonId(enrolledLessonId);
    }
}
