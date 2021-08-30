package com.spring.kodo.service;

import com.spring.kodo.entity.QuizQuestionOption;
import com.spring.kodo.entity.StudentAttempt;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface StudentAttemptService
{
    StudentAttempt createNewStudentAttempt(Long quizId, Long studentId) throws AccountNotFoundException, QuizNotFoundException, InputDataValidationException;

    StudentAttempt getStudentAttemptByStudentAttemptId(Long studentAttemptId) throws StudentAttemptNotFoundException;

    List<StudentAttempt> getAllStudentAttempts();

    void addStudentAttemptAnswerToStudentAttemptQuestion(Long studentAttemptQuestionId, List<Long> quizQuestionOptionIds) throws UpdateStudentAttemptAnswerException, StudentAttemptQuestionNotFoundException, QuizQuestionOptionNotFoundException;
}
