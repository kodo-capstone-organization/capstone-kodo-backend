package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Quiz;
import com.spring.kodo.repository.QuizRepository;
import com.spring.kodo.service.QuizService;
import com.spring.kodo.util.exception.QuizNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

@Service
public class QuizServiceImpl implements QuizService
{
    @Autowired
    private QuizRepository quizRepository;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public QuizServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Quiz createNewQuiz(Quiz quiz) throws InputDataValidationException
    {
        return null;
    }

    @Override
    public Quiz getQuizByQuizId(Long quizId) throws QuizNotFoundException
    {
        Quiz quiz = quizRepository.findById(quizId).orElse(null);

        if (quiz != null)
        {
            return quiz;
        }
        else
        {
            throw new QuizNotFoundException("Quiz with ID: " + quizId + " does not exist!");
        }
    }

    @Override
    public List<Quiz> getAllQuizs()
    {
        return quizRepository.findAll();
    }
}
