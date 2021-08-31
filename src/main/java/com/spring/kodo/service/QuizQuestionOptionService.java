package com.spring.kodo.service;

import com.spring.kodo.entity.QuizQuestionOption;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.QuizQuestionOptionNotFoundException;
import com.spring.kodo.util.exception.UnknownPersistenceException;

import java.util.List;

public interface QuizQuestionOptionService
{
    QuizQuestionOption createNewQuizQuestionOption(QuizQuestionOption newQuizQuestionOption) throws InputDataValidationException, UnknownPersistenceException;

    List<QuizQuestionOption> createNewQuizQuestionOptions(List<QuizQuestionOption> newQuizQuestionOptions) throws InputDataValidationException, UnknownPersistenceException;

    QuizQuestionOption getQuizQuestionOptionByQuizQuestionOptionId(Long quizQuestionOptionId) throws QuizQuestionOptionNotFoundException;

    QuizQuestionOption getQuizQuestionOptionByLeftContent(String leftContent) throws QuizQuestionOptionNotFoundException;

    QuizQuestionOption getQuizQuestionOptionByRightContent(String rightContent) throws QuizQuestionOptionNotFoundException;

    List<QuizQuestionOption> getAllQuizQuestionOptions();
}
