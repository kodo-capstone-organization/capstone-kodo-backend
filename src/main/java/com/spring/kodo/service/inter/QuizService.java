package com.spring.kodo.service.inter;

import com.spring.kodo.entity.Quiz;
import com.spring.kodo.entity.QuizQuestion;
import com.spring.kodo.util.exception.*;
import org.apache.http.MethodNotSupportedException;

import java.util.List;

public interface QuizService
{
    Quiz createNewQuiz(Quiz newQuiz) throws UnknownPersistenceException, CreateNewQuizException, InputDataValidationException;

    Quiz getQuizByQuizId(Long quizId) throws QuizNotFoundException;

    List<Quiz> getAllQuizzes();

    Quiz addQuizQuestionToQuiz(Quiz quiz, QuizQuestion quizQuestion) throws QuizNotFoundException, UpdateQuizException, UnknownPersistenceException, QuizQuestionNotFoundException;

    Quiz addQuizQuestionsToQuiz(Quiz quiz, List<QuizQuestion> quizQuestions) throws QuizNotFoundException, UpdateQuizException, QuizQuestionNotFoundException;

    Quiz updateQuiz(Quiz quiz) throws MethodNotSupportedException;
}
