package com.spring.kodo.service.inter;

import com.spring.kodo.entity.QuizQuestion;
import com.spring.kodo.entity.QuizQuestionOption;
import com.spring.kodo.util.enumeration.QuestionType;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface QuizQuestionService
{
    QuizQuestion createNewQuizQuestion(QuizQuestion newQuizQuestion, Long quizId) throws CreateNewQuizQuestionException, InputDataValidationException, UnknownPersistenceException;

    QuizQuestion getQuizQuestionByQuizQuestionId(Long quizId) throws QuizQuestionNotFoundException;

    QuizQuestion getQuizQuestionByContent(String content) throws QuizQuestionNotFoundException;

    QuizQuestion getQuizQuestionByQuestionType(QuestionType questionType) throws QuizQuestionNotFoundException;

    List<QuizQuestion> getAllQuizQuestions();

    List<QuizQuestion> getAllQuizQuestionsByQuizId(Long quizId);

    List<QuizQuestion> getAllQuizQuestionsByTutorId(Long tutorId);

    QuizQuestion addQuizQuestionOptionToQuizQuestion(QuizQuestion quizQuestion, QuizQuestionOption quizQuestionOption) throws UpdateQuizQuestionException, QuizQuestionNotFoundException, QuizQuestionOptionNotFoundException;

    QuizQuestion addQuizQuestionOptionsToQuizQuestion(QuizQuestion quizQuestion, List<QuizQuestionOption> quizQuestionOptions) throws UpdateQuizQuestionException, QuizQuestionNotFoundException, QuizQuestionOptionNotFoundException;

    Boolean deleteQuizQuestionByQuizQuestionId(Long quizQuestionId) throws DeleteQuizQuestionException, QuizQuestionNotFoundException;

    Boolean isQuizQuestionContainsStudentAttemptQuestionsByQuizQuestionId(Long quizQuestionId);
}
