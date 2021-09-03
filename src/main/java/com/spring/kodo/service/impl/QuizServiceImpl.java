package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Quiz;
import com.spring.kodo.entity.QuizQuestion;
import com.spring.kodo.repository.QuizRepository;
import com.spring.kodo.service.inter.QuizQuestionService;
import com.spring.kodo.service.inter.QuizService;
import com.spring.kodo.util.MessageFormatterUtil;
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
                    throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
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
}
