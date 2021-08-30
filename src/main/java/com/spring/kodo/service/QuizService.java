package com.spring.kodo.service;

import com.spring.kodo.entity.Quiz;
import com.spring.kodo.entity.QuizQuestion;
import com.spring.kodo.entity.QuizQuestionOption;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface QuizService
{
    Quiz createNewQuiz(Quiz newQuiz) throws InputDataValidationException, CreateQuizException;

    Quiz getQuizByQuizId(Long quizId) throws QuizNotFoundException;

    List<Quiz> getAllQuizzes();

    Quiz addQuizQuestionToQuiz(Quiz quiz, QuizQuestion quizQuestion, List<QuizQuestionOption> quizQuestionOptions) throws QuizNotFoundException, UpdateQuizException;
}