package com.spring.kodo.service;

import com.spring.kodo.entity.StudentAttempt;
import com.spring.kodo.util.exception.AccountNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.QuizNotFoundException;
import com.spring.kodo.util.exception.StudentAttemptNotFoundException;

import java.util.List;

public interface StudentAttemptService
{
    StudentAttempt createNewStudentAttempt(Long quizId, Long studentId) throws AccountNotFoundException, QuizNotFoundException, InputDataValidationException;

    StudentAttempt getStudentAttemptByStudentAttemptId(Long studentAttemptId) throws StudentAttemptNotFoundException;

    List<StudentAttempt> getAllStudentAttempts();
}
