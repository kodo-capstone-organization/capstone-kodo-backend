package com.spring.kodo.service.inter;

import com.spring.kodo.entity.QuizQuestionOption;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface QuizQuestionOptionService
{
    QuizQuestionOption createNewQuizQuestionOption(QuizQuestionOption newQuizQuestionOption) throws InputDataValidationException, UnknownPersistenceException;

    List<QuizQuestionOption> createNewQuizQuestionOptions(List<QuizQuestionOption> newQuizQuestionOptions) throws InputDataValidationException, UnknownPersistenceException;

    QuizQuestionOption getQuizQuestionOptionByQuizQuestionOptionId(Long quizQuestionOptionId) throws QuizQuestionOptionNotFoundException;

    QuizQuestionOption getQuizQuestionOptionByLeftContent(String leftContent) throws QuizQuestionOptionNotFoundException;

    QuizQuestionOption getQuizQuestionOptionByRightContent(String rightContent) throws QuizQuestionOptionNotFoundException;

    List<QuizQuestionOption> getAllQuizQuestionOptions();

    QuizQuestionOption updateQuizQuestionOption(QuizQuestionOption quizQuestionOption) throws UpdateQuizQuestionOptionException, QuizQuestionOptionNotFoundException, InputDataValidationException;

    Boolean deleteQuizQuestionOptionByQuizQuestionOptionId(Long quizQuestionOptionId) throws DeleteQuizQuestionOptionException, QuizQuestionOptionNotFoundException;

//    Boolean isQuizQuestionOptionContainsStudentAttemptAnswersByQuizQuestionOptionId(Long quizQuestionOptionId);
}
