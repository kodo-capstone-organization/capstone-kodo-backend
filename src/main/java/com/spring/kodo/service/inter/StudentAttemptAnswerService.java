package com.spring.kodo.service.inter;

import com.spring.kodo.entity.StudentAttempt;
import com.spring.kodo.entity.StudentAttemptAnswer;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface StudentAttemptAnswerService
{
    StudentAttemptAnswer createNewStudentAttemptAnswer(Long leftQuizQuestionOptionId) throws InputDataValidationException, CreateNewStudentAttemptAnswerException, QuizQuestionOptionNotFoundException, UnknownPersistenceException;

    StudentAttemptAnswer createNewStudentAttemptAnswer(Long leftQuizQuestionOptionId, Long rightQuizQuestionOptionId) throws InputDataValidationException, CreateNewStudentAttemptAnswerException, QuizQuestionOptionNotFoundException, UnknownPersistenceException;

    StudentAttemptAnswer getStudentAttemptAnswerByStudentAttemptAnswerId(Long studentAttemptAnswerId) throws StudentAttemptAnswerNotFoundException;

    List<StudentAttemptAnswer> getAllStudentAttemptAnswers();

    StudentAttemptAnswer updateStudentAttemptAnswer(StudentAttemptAnswer studentAttemptAnswer) throws UpdateStudentAttemptAnswerException, StudentAttemptAnswerNotFoundException;
}
