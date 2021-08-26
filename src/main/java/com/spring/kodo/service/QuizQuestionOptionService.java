package com.spring.kodo.service;

import com.spring.kodo.entity.QuizQuestionOption;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.QuizQuestionOptionNotFoundException;

import java.util.List;

public interface QuizQuestionOptionService
{
    QuizQuestionOption createNewQuizQuestionOption(QuizQuestionOption quizQuestionOption) throws InputDataValidationException;

    QuizQuestionOption getQuizQuestionOptionByQuizQuestionOptionId(Long quizQuestionOptionId) throws QuizQuestionOptionNotFoundException;

    QuizQuestionOption getQuizQuestionOptionByLeftContent(String leftContent) throws QuizQuestionOptionNotFoundException;

    QuizQuestionOption getQuizQuestionOptionByRightContent(String rightContent) throws QuizQuestionOptionNotFoundException;

    List<QuizQuestionOption> getAllQuizQuestionOptions();
}
