package com.spring.kodo.service;

import com.spring.kodo.entity.StudentAttemptAnswer;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface StudentAttemptAnswerService
{
    StudentAttemptAnswer createNewStudentAttemptAnswer(Long quizQuestionOptionId) throws InputDataValidationException, CreateStudentAttemptAnswerException, QuizQuestionOptionNotFoundException, UnknownPersistenceException;

    StudentAttemptAnswer getStudentAttemptAnswerByStudentAttemptAnswerId(Long studentAttemptAnswerId) throws StudentAttemptAnswerNotFoundException;

    List<StudentAttemptAnswer> getAllStudentAttemptAnswers();
}
