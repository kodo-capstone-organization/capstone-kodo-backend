package com.spring.kodo.service;

import com.spring.kodo.entity.QuizQuestion;
import com.spring.kodo.entity.QuizQuestionOption;
import com.spring.kodo.util.enumeration.QuestionType;
import com.spring.kodo.util.exception.CreateQuizQuestionException;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.QuizQuestionNotFoundException;

import java.util.List;

public interface QuizQuestionService
{
    QuizQuestion createNewQuizQuestion(QuizQuestion newQuizQuestion, Long quizId, List<QuizQuestionOption> quizQuestionOptions) throws CreateQuizQuestionException, InputDataValidationException;

    QuizQuestion getQuizQuestionByQuizQuestionId(Long quizId) throws QuizQuestionNotFoundException;

    QuizQuestion getQuizQuestionByContent(String content) throws QuizQuestionNotFoundException;

    QuizQuestion getQuizQuestionByQuestionType(QuestionType questionType) throws QuizQuestionNotFoundException;

    List<QuizQuestion> getAllQuizQuestions();
}
