package com.spring.kodo.service;

import com.spring.kodo.entity.StudentAttemptAnswer;
import com.spring.kodo.util.exception.CreateStudentAttemptAnswerException;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.QuizQuestionOptionNotFoundException;
import com.spring.kodo.util.exception.StudentAttemptAnswerNotFoundException;

import java.util.List;

public interface StudentAttemptAnswerService
{
    StudentAttemptAnswer createNewStudentAttemptAnswer(Long quizQuestionOptionId) throws InputDataValidationException, CreateStudentAttemptAnswerException, QuizQuestionOptionNotFoundException;

    StudentAttemptAnswer getStudentAttemptAnswerByStudentAttemptAnswerId(Long studentAttemptAnswerId) throws StudentAttemptAnswerNotFoundException;

    List<StudentAttemptAnswer> getAllStudentAttemptAnswers();
}
