package com.spring.kodo.service.impl;

import com.spring.kodo.entity.*;
import com.spring.kodo.repository.QuizQuestionRepository;
import com.spring.kodo.repository.QuizRepository;
import com.spring.kodo.service.LessonService;
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
    private LessonService lessonService;

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
    public Quiz createNewQuiz(Quiz newQuiz) throws InputDataValidationException, CreateQuizException
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
    public Quiz addQuizQuestionToQuiz(Quiz quiz, QuizQuestion quizQuestion, List<QuizQuestionOption> quizQuestionOptions) throws QuizNotFoundException, UpdateQuizException
    {
        quiz = getQuizByQuizId(quiz.getContentId());
        if (!quiz.getQuizQuestions().contains(quizQuestion))
        {
            try
            {
                quizQuestionService.createNewQuizQuestion(quizQuestion, quiz.getContentId(), quizQuestionOptions);
            }
            catch (CreateQuizQuestionException | InputDataValidationException ex)
            {
                throw new UpdateQuizException(ex.getMessage());
            }
        }
        else
        {
            throw new UpdateQuizException("Quiz: " + quiz.getName() + " already has question with Content: " + quizQuestion.getContent());
        }

        quizRepository.saveAndFlush(quiz);
        return quiz;
    }
}
