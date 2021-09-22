package com.spring.kodo.controller;

import com.spring.kodo.entity.QuizQuestion;
import com.spring.kodo.entity.QuizQuestionOption;
import com.spring.kodo.service.inter.QuizQuestionOptionService;
import com.spring.kodo.util.exception.QuizQuestionOptionNotFoundException;
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
@RequestMapping(path = "/quizQuestionOption")
public class QuizQuestionOptionController
{
    Logger logger = LoggerFactory.getLogger(QuizQuestionOptionController.class);

    @Autowired
    private QuizQuestionOptionService quizQuestionOptionService;

    @GetMapping("/getQuizQuestionOptionByQuizQuestionOptionId/{quizQuestionOptionId}")
    public QuizQuestionOption getQuizQuestionOptionByQuizQuestionOptionId(@PathVariable Long quizQuestionOptionId)
    {
        try
        {
            return this.quizQuestionOptionService.getQuizQuestionOptionByQuizQuestionOptionId(quizQuestionOptionId);
        }
        catch (QuizQuestionOptionNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllQuizQuestionOptions")
    public List<QuizQuestionOption> getAllQuizQuestionOptions()
    {
        return this.quizQuestionOptionService.getAllQuizQuestionOptions();
    }
}
