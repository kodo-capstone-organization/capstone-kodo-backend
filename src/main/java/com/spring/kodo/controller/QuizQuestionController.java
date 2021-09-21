package com.spring.kodo.controller;

import com.spring.kodo.entity.QuizQuestion;
import com.spring.kodo.service.inter.QuizQuestionService;
import com.spring.kodo.util.exception.QuizQuestionNotFoundException;
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
@RequestMapping(path = "/quizQuestion")
public class QuizQuestionController
{
    Logger logger = LoggerFactory.getLogger(QuizQuestionController.class);

    @Autowired
    private QuizQuestionService quizQuestionService;

    @GetMapping("/getQuizQuestionByQuizQuestionId/{quizId}")
    public QuizQuestion getQuizQuestionByQuizQuestionId(@PathVariable Long quizId)
    {
        try
        {
            return this.quizQuestionService.getQuizQuestionByQuizQuestionId(quizId);
        }
        catch (QuizQuestionNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllQuizQuestions")
    public List<QuizQuestion> getAllQuizQuestions()
    {
        return this.quizQuestionService.getAllQuizQuestions();
    }

    @GetMapping("/getAllQuizQuestionsByQuizId/{quizId}")
    public List<QuizQuestion> getAllQuizQuestionsByQuizId(@PathVariable("quizId") Long quizId)
    {
        return this.quizQuestionService.getAllQuizQuestionsByQuizId(quizId);
    }

    @GetMapping("/getAllQuizQuestionsByTutorId/{tutorId}")
    public List<QuizQuestion> getAllQuizQuestionsByTutorId(@PathVariable("tutorId") Long tutorId)
    {
        return this.quizQuestionService.getAllQuizQuestionsByTutorId(tutorId);
    }
}
