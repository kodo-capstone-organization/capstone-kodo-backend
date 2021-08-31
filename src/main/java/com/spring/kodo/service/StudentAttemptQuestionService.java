package com.spring.kodo.service;

import com.spring.kodo.entity.StudentAttemptAnswer;
import com.spring.kodo.entity.StudentAttemptQuestion;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface StudentAttemptQuestionService
{
    StudentAttemptQuestion createNewStudentAttemptQuestion(Long quizQuestionId) throws InputDataValidationException, CreateStudentAttemptQuestionException, QuizQuestionNotFoundException;

    StudentAttemptQuestion getStudentAttemptQuestionByStudentAttemptQuestionId(Long studentAttemptQuestionId) throws StudentAttemptQuestionNotFoundException;

    List<StudentAttemptQuestion> getAllStudentAttemptQuestions();
    StudentAttemptQuestion addStudentAttemptAnswerToStudentAttemptQuestion(StudentAttemptQuestion studentAttemptQuestion, StudentAttemptAnswer studentAttemptAnswer) throws UpdateStudentAttemptQuestionException, StudentAttemptQuestionNotFoundException, StudentAttemptAnswerNotFoundException;
}
