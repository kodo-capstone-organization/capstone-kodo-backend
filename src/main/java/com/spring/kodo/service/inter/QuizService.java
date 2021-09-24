package com.spring.kodo.service.inter;

import com.spring.kodo.entity.Quiz;
import com.spring.kodo.entity.QuizQuestion;
import com.spring.kodo.entity.QuizQuestionOption;
import com.spring.kodo.restentity.response.QuizWithStudentAttemptCountResp;
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

    Quiz updateQuiz(Quiz quiz) throws UpdateQuizException, QuizNotFoundException, InputDataValidationException;

    Quiz updateQuiz(Quiz quiz, List<QuizQuestion> quizQuestions, List<List<QuizQuestionOption>> quizQuestionOptionLists) throws QuizNotFoundException, UpdateQuizException, InputDataValidationException, QuizQuestionOptionNotFoundException, QuizQuestionNotFoundException, UpdateQuizQuestionException, UpdateQuizQuestionOptionException, CreateNewQuizQuestionException, UnknownPersistenceException;

    List<QuizWithStudentAttemptCountResp> getAllQuizzesWithStudentAttemptCountByEnrolledLessonId(Long enrolledLessonId);

    Boolean deleteQuizzesWithQuizQuestionsAndQuizQuestionOptionsByQuizId(List<Long> quizIds) throws DeleteQuizException, QuizNotFoundException, QuizQuestionOptionNotFoundException, DeleteQuizQuestionOptionException, QuizQuestionNotFoundException, DeleteQuizQuestionException, LessonNotFoundException, UpdateContentException, InputDataValidationException, ContentNotFoundException, UnknownPersistenceException;
}
