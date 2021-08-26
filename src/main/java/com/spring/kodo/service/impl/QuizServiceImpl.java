package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Quiz;
import com.spring.kodo.entity.QuizQuestion;
import com.spring.kodo.repository.QuizQuestionRepository;
import com.spring.kodo.repository.QuizRepository;
import com.spring.kodo.service.ContentService;
import com.spring.kodo.service.QuizQuestionService;
import com.spring.kodo.service.QuizService;
import com.spring.kodo.util.MessageFormatterUtil;
import com.spring.kodo.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private ContentService contentService;
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
    public Quiz createNewQuiz(Quiz quiz, List<Long> quizQuestionIds) throws InputDataValidationException, UnknownPersistenceException, QuizCreateException, QuizUpdateException, QuizNotFoundException, QuizQuestionNotFoundException
    {
        Set<ConstraintViolation<Quiz>> constraintViolations = validator.validate(quiz);
        if(constraintViolations.isEmpty())
        {
            if (quizQuestionIds != null && (!quizQuestionIds.isEmpty()))
            {
                Quiz newQuiz = (Quiz) contentService.createNewContent(quiz);

                for (Long quizQuestionId: quizQuestionIds)
                {
                    newQuiz = addQuizQuestionToQuiz(newQuiz.getContentId(), quizQuestionId); // bidirectional
                }

                return quizRepository.saveAndFlush(newQuiz);
            }
            else
            {
                throw new QuizCreateException("A quiz must have questions before it can be created");
            }
        }
        else
        {
            throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public Quiz addQuizQuestionToQuiz(Long quizId, Long quizQuestionId) throws QuizNotFoundException, QuizQuestionNotFoundException, QuizUpdateException {

        Quiz quiz = getQuizByQuizId(quizId);
        QuizQuestion quizQuestion = quizQuestionService.getQuizQuestionByQuizQuestionId(quizQuestionId);

        if (quiz.getQuestions().contains(quizQuestion))
        {
            quiz.getQuestions().add(quizQuestion);
            quizQuestion.setQuiz(quiz);
        }
        else
        {
            throw new QuizUpdateException("Quiz: " + quiz.getName() + " already has question with ID: " + quizQuestion.getQuizQuestionId());
        }

        quizRepository.saveAndFlush(quiz);
        quizQuestionRepository.saveAndFlush(quizQuestion);

        return quiz;
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
}
