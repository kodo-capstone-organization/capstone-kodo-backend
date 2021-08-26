package com.spring.kodo.service;

import com.spring.kodo.entity.StudentAttempt;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.StudentAttemptNotFoundException;

import java.util.List;

public interface StudentAttemptService
{
    StudentAttempt createNewStudentAttempt(StudentAttempt studentAttempt) throws InputDataValidationException;

    StudentAttempt getStudentAttemptByStudentAttemptId(Long studentAttemptId) throws StudentAttemptNotFoundException;

    List<StudentAttempt> getAllStudentAttempts();
}
