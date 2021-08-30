package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Quiz;
import com.spring.kodo.entity.QuizQuestion;
import com.spring.kodo.entity.QuizQuestionOption;
import com.spring.kodo.repository.QuizQuestionOptionRepository;
import com.spring.kodo.repository.QuizQuestionRepository;
import com.spring.kodo.service.QuizQuestionService;
import com.spring.kodo.service.QuizService;
import com.spring.kodo.util.MessageFormatterUtil;
import com.spring.kodo.util.enumeration.QuestionType;
import com.spring.kodo.util.exception.CreateQuizQuestionException;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.QuizNotFoundException;
import com.spring.kodo.util.exception.QuizQuestionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class QuizQuestionServiceImpl implements QuizQuestionService
{
    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private QuizQuestionOptionRepository quizQuestionOptionRepository;

    @Autowired
    private QuizService quizService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public QuizQuestionServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public QuizQuestion createNewQuizQuestion(QuizQuestion newQuizQuestion, Long quizId, List<QuizQuestionOption> quizQuestionOptions) throws CreateQuizQuestionException, InputDataValidationException
    {
        Set<ConstraintViolation<QuizQuestion>> constraintViolations = validator.validate(newQuizQuestion);
        if (constraintViolations.isEmpty())
        {
            try
            {
                Quiz quiz = quizService.getQuizByQuizId(quizId);
                quiz.getQuizQuestions().add(newQuizQuestion);
                newQuizQuestion.setQuiz(quiz);

                for (QuizQuestionOption quizQuestionOption : quizQuestionOptions)
                {
                    newQuizQuestion.getQuizQuestionOptions().add(quizQuestionOption);
                    quizQuestionOptionRepository.save(quizQuestionOption);
                }
            }
            catch (QuizNotFoundException ex)
            {
                throw new CreateQuizQuestionException(ex.getMessage());
            }

            quizQuestionRepository.saveAndFlush(newQuizQuestion);
            return newQuizQuestion;
        }
        else
        {
            throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public QuizQuestion getQuizQuestionByQuizQuestionId(Long quizQuestionId) throws QuizQuestionNotFoundException
    {
        QuizQuestion quizQuestion = quizQuestionRepository.findById(quizQuestionId).orElse(null);

        if (quizQuestion != null)
        {
            return quizQuestion;
        }
        else
        {
            throw new QuizQuestionNotFoundException("QuizQuestion with ID: " + quizQuestionId + " does not exist!");
        }
    }

    @Override
    public QuizQuestion getQuizQuestionByContent(String content) throws QuizQuestionNotFoundException
    {
        QuizQuestion quizQuestion = quizQuestionRepository.findByContent(content).orElse(null);

        if (quizQuestion != null)
        {
            return quizQuestion;
        }
        else
        {
            throw new QuizQuestionNotFoundException("QuizQuestion with Content: " + content + " does not exist!");
        }
    }


    @Override
    public QuizQuestion getQuizQuestionByQuestionType(QuestionType questionType) throws QuizQuestionNotFoundException
    {
        QuizQuestion quizQuestion = quizQuestionRepository.findByQuestionType(questionType).orElse(null);

        if (quizQuestion != null)
        {
            return quizQuestion;
        }
        else
        {
            throw new QuizQuestionNotFoundException("QuizQuestion with QuestionType: " + questionType + " does not exist!");
        }
    }

    @Override
    public List<QuizQuestion> getAllQuizQuestions()
    {
        return quizQuestionRepository.findAll();
    }
}
