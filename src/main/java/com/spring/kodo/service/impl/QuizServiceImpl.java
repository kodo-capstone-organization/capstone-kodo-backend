package com.spring.kodo.service.impl;

import com.spring.kodo.entity.*;
import com.spring.kodo.repository.QuizRepository;
import com.spring.kodo.entity.rest.response.QuizWithStudentAttemptCountResp;
import com.spring.kodo.service.inter.LessonService;
import com.spring.kodo.service.inter.QuizQuestionOptionService;
import com.spring.kodo.service.inter.QuizQuestionService;
import com.spring.kodo.service.inter.QuizService;
import com.spring.kodo.util.helper.FormatterHelper;
import com.spring.kodo.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class QuizServiceImpl implements QuizService
{
    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizQuestionService quizQuestionService;

    @Autowired
    private QuizQuestionOptionService quizQuestionOptionService;

    @Autowired
    private LessonService lessonService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public QuizServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Quiz createNewQuiz(Quiz newQuiz) throws UnknownPersistenceException, CreateNewQuizException, InputDataValidationException
    {
        try
        {
            if (newQuiz != null)
            {
                Set<ConstraintViolation<Quiz>> constraintViolations = validator.validate(newQuiz);
                if (constraintViolations.isEmpty())
                {
                    // Persist before adding in quizQuestions
                    quizRepository.saveAndFlush(newQuiz);
                    return newQuiz;
                }
                else
                {
                    throw new InputDataValidationException(FormatterHelper.prepareInputDataValidationErrorsMessage(constraintViolations));
                }
            }
            else
            {
                throw new CreateNewQuizException("New Quiz cannot be null");
            }
        }
        catch (DataAccessException ex)
        {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    @Override
    public Quiz getQuizByQuizId(Long quizId) throws QuizNotFoundException
    {
        Quiz quiz = quizRepository.findById(quizId).orElse(null);

        if (quiz != null)
        {
            return quiz;
        }
        else
        {
            throw new QuizNotFoundException("Quiz with ID: " + quizId + " does not exist!");
        }
    }

    @Override
    public Quiz getQuizByEnrolledContentId(Long enrolledContentId) throws QuizNotFoundException
    {
        Quiz quiz = quizRepository.findByEnrolledContentId(enrolledContentId).orElse(null);

        if (quiz != null)
        {
            return quiz;
        }
        else
        {
            throw new QuizNotFoundException("Quiz with EnrolledContent ID: " + enrolledContentId + " does not exist!");
        }
    }

    @Override
    public List<Quiz> getAllQuizzes()
    {
        return quizRepository.findAll();
    }

    @Override
    public Quiz addQuizQuestionToQuiz(Quiz quiz, QuizQuestion quizQuestion) throws QuizNotFoundException, UpdateQuizException, QuizQuestionNotFoundException
    {
        if (quiz != null)
        {
            if (quiz.getContentId() != null)
            {
                quiz = getQuizByQuizId(quiz.getContentId());

                if (quizQuestion != null)
                {
                    if (quizQuestion.getQuizQuestionId() != null)
                    {
                        quizQuestion = quizQuestionService.getQuizQuestionByQuizQuestionId(quizQuestion.getQuizQuestionId());
                        quiz.getQuizQuestions().add(quizQuestion);

                        quizRepository.saveAndFlush(quiz);
                        return quiz;
                    }
                    else
                    {
                        throw new UpdateQuizException("QuizQuestion ID cannot be null");
                    }
                }
                else
                {
                    throw new UpdateQuizException("QuizQuestion cannot be null");
                }
            }
            else
            {
                throw new UpdateQuizException("Quiz ID cannot be null");
            }
        }
        else
        {
            throw new UpdateQuizException("Quiz cannot be null");
        }
    }

    @Override
    public Quiz addQuizQuestionsToQuiz(Quiz quiz, List<QuizQuestion> quizQuestions) throws QuizNotFoundException, UpdateQuizException, QuizQuestionNotFoundException
    {
        if (quiz != null)
        {
            if (quiz.getContentId() != null)
            {
                quiz = getQuizByQuizId(quiz.getContentId());

                for (int i = 0; i < quizQuestions.size(); i++)
                {
                    QuizQuestion quizQuestion = quizQuestions.get(i);
                    if (quizQuestion != null)
                    {
                        if (quizQuestion.getQuizQuestionId() != null)
                        {
                            quizQuestion = quizQuestionService.getQuizQuestionByQuizQuestionId(quizQuestion.getQuizQuestionId());
                            quiz.getQuizQuestions().add(quizQuestion);

                            quizQuestions.set(i, quizQuestion);
                        }
                        else
                        {
                            throw new UpdateQuizException("QuizQuestion ID cannot be null");
                        }
                    }
                    else
                    {
                        throw new UpdateQuizException("QuizQuestion cannot be null");
                    }
                }

                quizRepository.saveAndFlush(quiz);
                return quiz;
            }
            else
            {
                throw new UpdateQuizException("Quiz ID cannot be null");
            }
        }
        else
        {
            throw new UpdateQuizException("Quiz cannot be null");
        }
    }

    @Override
    public Quiz updateQuiz(Quiz quiz) throws UpdateQuizException, QuizNotFoundException, InputDataValidationException
    {
        if (quiz != null)
        {
            if (quiz.getContentId() != null)
            {
                Set<ConstraintViolation<Quiz>> constraintViolations = validator.validate(quiz);

                if (constraintViolations.isEmpty())
                {
                    Quiz quizToUpdate = getQuizByQuizId(quiz.getContentId());

                    quizToUpdate.setName(quiz.getName());
                    quizToUpdate.setDescription(quiz.getDescription());
                    quizToUpdate.setTimeLimit(quiz.getTimeLimit());
                    quizToUpdate.setMaxAttemptsPerStudent(quiz.getMaxAttemptsPerStudent());

                    quizRepository.saveAndFlush(quizToUpdate);
                    return quizToUpdate;
                }
                else
                {
                    throw new InputDataValidationException(FormatterHelper.prepareInputDataValidationErrorsMessage(constraintViolations));
                }
            }
            else
            {
                throw new UpdateQuizException("Quiz ID cannot be null");
            }
        }
        else
        {
            throw new UpdateQuizException("Quiz cannot be null");
        }
    }

    @Override
    public Quiz updateQuiz(Quiz quiz, List<QuizQuestion> quizQuestions, List<List<QuizQuestionOption>> quizQuestionOptionLists) throws UpdateQuizException, QuizNotFoundException, InputDataValidationException, QuizQuestionOptionNotFoundException, QuizQuestionNotFoundException, DeleteQuizQuestionOptionException, DeleteQuizQuestionException, CreateNewQuizQuestionException, UnknownPersistenceException, UpdateQuizQuestionException, CreateNewQuizQuestionOptionException
    {
        if (quiz != null)
        {
            if (quizQuestions != null)
            {
                if (quizQuestionOptionLists != null)
                {
                    if (quizQuestions.size() == quizQuestionOptionLists.size())
                    {
                        Quiz quizToUpdate = updateQuiz(quiz);

                        // Deletion
                        // Includes deleting QuizQuestionOptions
                        List<QuizQuestion> quizQuestionsToDelete = quizToUpdate.getQuizQuestions();
                        QuizQuestion quizQuestionToDelete;

                        for (int i = 0; i < quizQuestionsToDelete.size(); i++)
                        {
                            quizQuestionToDelete = quizQuestionsToDelete.get(i);
                            quizQuestionService.deleteQuizQuestionWithQuizQuestionOptionsByQuizQuestionId(quizQuestionToDelete.getQuizQuestionId());
                        }
                        quizToUpdate.getQuizQuestions().clear();

                        // Recreation
                        QuizQuestion quizQuestion;
                        List<QuizQuestionOption> quizQuestionOptions;

                        for (int i = 0; i < quizQuestions.size(); i++)
                        {
                            quizQuestion = quizQuestions.get(i);
                            quizQuestionOptions = quizQuestionOptionLists.get(i);

                            quizQuestion = quizQuestionService.createNewQuizQuestion(quizQuestion, quizToUpdate.getContentId());
                            quizQuestionOptions = quizQuestionOptionService.createNewQuizQuestionOptions(quizQuestionOptions);

                            quizQuestion = quizQuestionService.addQuizQuestionOptionsToQuizQuestion(quizQuestion, quizQuestionOptions);
                            quizToUpdate = addQuizQuestionToQuiz(quizToUpdate, quizQuestion);
                        }

                        quizRepository.saveAndFlush(quizToUpdate);
                        return quizToUpdate;
                    }
                    else
                    {
                        throw new UpdateQuizException("QuizQuestions and QuizQuestionOptionLists have to have the same size");
                    }
                }
                else
                {
                    throw new UpdateQuizException("Quiz Questions cannot be null");
                }
            }
            else
            {
                throw new UpdateQuizException("QuizQuestionOptionLists cannot be null");
            }
        }
        else
        {
            throw new UpdateQuizException("Quiz cannot be null");
        }
    }

    @Override
    public List<QuizWithStudentAttemptCountResp> getAllQuizzesWithStudentAttemptCountByEnrolledLessonId(Long enrolledLessonId)
    {
        return quizRepository.findAllQuizzesWithStudentAttemptCountByEnrolledLessonId(enrolledLessonId);
    }

    @Override
    public Boolean deleteQuizzesWithQuizQuestionsAndQuizQuestionOptionsByQuizId(List<Long> quizIds) throws DeleteQuizException, QuizNotFoundException, QuizQuestionOptionNotFoundException, DeleteQuizQuestionOptionException, QuizQuestionNotFoundException, DeleteQuizQuestionException, LessonNotFoundException, UpdateContentException, InputDataValidationException, ContentNotFoundException, UnknownPersistenceException
    {
        for (Long quizId : quizIds)
        {
            Quiz quizToDelete = getQuizByQuizId(quizId);

            if (quizToDelete.getStudentAttempts().size() == 0)
            {
                for (QuizQuestion quizQuestion : quizToDelete.getQuizQuestions())
                {
                    for (QuizQuestionOption quizQuestionOption : quizQuestion.getQuizQuestionOptions())
                    {
                        quizQuestionOptionService.deleteQuizQuestionOptionByQuizQuestionOptionId(quizQuestionOption.getQuizQuestionOptionId());
                    }
                    quizQuestion.getQuizQuestionOptions().clear();
                    quizQuestionService.deleteQuizQuestionByQuizQuestionId(quizQuestion.getQuizQuestionId());
                }
                quizToDelete.getQuizQuestions().clear();

                Lesson lesson = this.lessonService.getLessonByContentId(quizId);

                List<Content> contents = lesson.getContents();
                List<Long> updatedContentIds = contents.stream().filter((Content content) -> !content.getContentId().equals(quizId)).map((Content content) -> content.getContentId()).toList();

                this.lessonService.updateLesson(lesson, updatedContentIds);

                quizRepository.delete(quizToDelete);
            }
            else
            {
                throw new DeleteQuizException("Quiz that has StudentAttempts cannot be deleted");
            }
        }
        return true;
    }
}
