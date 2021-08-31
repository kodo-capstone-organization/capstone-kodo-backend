package com.spring.kodo.service;

import com.spring.kodo.entity.QuizQuestionOption;
import com.spring.kodo.entity.StudentAttempt;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface StudentAttemptService
{
    StudentAttempt createNewStudentAttempt(Long quizId) throws CreateStudentAttemptException, QuizNotFoundException, InputDataValidationException, CreateStudentAttemptQuestionException, QuizQuestionNotFoundException, UnknownPersistenceException;

    StudentAttempt getStudentAttemptByStudentAttemptId(Long studentAttemptId) throws StudentAttemptNotFoundException;

    List<StudentAttempt> getAllStudentAttempts();
}
