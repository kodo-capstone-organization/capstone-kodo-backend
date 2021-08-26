package com.spring.kodo.service;

import com.spring.kodo.entity.Quiz;
import com.spring.kodo.util.exception.QuizNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;

import java.util.List;

public interface QuizService
{
    Quiz createNewQuiz(Quiz quiz) throws InputDataValidationException;

    Quiz getQuizByQuizId(Long quizId) throws QuizNotFoundException;

    List<Quiz> getAllQuizs();    
}
