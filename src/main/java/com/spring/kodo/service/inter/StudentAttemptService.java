package com.spring.kodo.service.inter;

import com.spring.kodo.entity.StudentAttempt;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface StudentAttemptService
{
    StudentAttempt createNewStudentAttempt(Long quizId) throws CreateNewStudentAttemptException, QuizNotFoundException, InputDataValidationException, CreateNewStudentAttemptQuestionException, QuizQuestionNotFoundException, UnknownPersistenceException;

    StudentAttempt getStudentAttemptByStudentAttemptId(Long studentAttemptId) throws StudentAttemptNotFoundException;

    List<StudentAttempt> getAllStudentAttempts();

    Integer getNumberOfStudentAttemptsLeft(Long accountId, Long quizId) throws AccountNotFoundException, QuizNotFoundException;

    Long deleteStudentAttemptByStudentAttemptId(Long studentAttemptId) throws StudentAttemptNotFoundException;

    Boolean isStudentAttemptCompleted(Long studentAttemptId) throws StudentAttemptNotFoundException;

    StudentAttempt markStudentAttemptByStudentAttemptId(Long studentAttemptId) throws StudentAttemptNotFoundException, UpdateStudentAttemptAnswerException, StudentAttemptAnswerNotFoundException;
}
