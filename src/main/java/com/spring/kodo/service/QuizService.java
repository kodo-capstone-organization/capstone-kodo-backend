package com.spring.kodo.service;

import com.spring.kodo.entity.Quiz;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface QuizService
{
    Quiz createNewQuiz(Quiz quiz, List<Long> quizQuestionIds) throws InputDataValidationException, UnknownPersistenceException, QuizCreateException, QuizUpdateException, QuizNotFoundException, QuizQuestionNotFoundException;

    Quiz addQuizQuestionToQuiz(Long quizId, Long quizQuestionId) throws QuizNotFoundException, QuizQuestionNotFoundException, QuizUpdateException;

    Quiz getQuizByQuizId(Long quizId) throws QuizNotFoundException;

    List<Quiz> getAllQuizzes();
}
