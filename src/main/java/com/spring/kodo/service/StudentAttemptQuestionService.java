package com.spring.kodo.service;

import com.spring.kodo.entity.StudentAttemptQuestion;
import com.spring.kodo.util.exception.StudentAttemptQuestionNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;

import java.util.List;

public interface StudentAttemptQuestionService
{
    StudentAttemptQuestion createNewStudentAttemptQuestion(StudentAttemptQuestion studentAttemptQuestion) throws InputDataValidationException;

    StudentAttemptQuestion getStudentAttemptQuestionByStudentAttemptQuestionId(Long studentAttemptQuestionId) throws StudentAttemptQuestionNotFoundException;

    List<StudentAttemptQuestion> getAllStudentAttemptQuestions();
}
