package com.spring.kodo.service.impl;

import com.spring.kodo.entity.QuizQuestionOption;
import com.spring.kodo.repository.QuizQuestionOptionRepository;
import com.spring.kodo.service.inter.QuizQuestionOptionService;
import com.spring.kodo.util.MessageFormatterUtil;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.QuizQuestionOptionNotFoundException;
import com.spring.kodo.util.exception.UnknownPersistenceException;
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
public class QuizQuestionOptionServiceImpl implements QuizQuestionOptionService
{
    @Autowired
    private QuizQuestionOptionRepository newQuizQuestionOptionRepository;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public QuizQuestionOptionServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public QuizQuestionOption createNewQuizQuestionOption(QuizQuestionOption newQuizQuestionOption) throws InputDataValidationException, UnknownPersistenceException
    {
        try
        {
            Set<ConstraintViolation<QuizQuestionOption>> constraintViolations = validator.validate(newQuizQuestionOption);
            if (constraintViolations.isEmpty())
            {
                newQuizQuestionOptionRepository.saveAndFlush(newQuizQuestionOption);
                return newQuizQuestionOption;
            }
            else
            {
                throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        catch (DataAccessException ex)
        {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    @Override
    public List<QuizQuestionOption> createNewQuizQuestionOptions(List<QuizQuestionOption> newQuizQuestionOptions) throws InputDataValidationException, UnknownPersistenceException
    {
        try
        {
            for (int i = 0; i < newQuizQuestionOptions.size(); i++)
            {
                QuizQuestionOption newQuizQuestionOption = newQuizQuestionOptions.get(i);
                Set<ConstraintViolation<QuizQuestionOption>> constraintViolations = validator.validate(newQuizQuestionOption);
                if (constraintViolations.isEmpty())
                {
                    newQuizQuestionOptionRepository.saveAndFlush(newQuizQuestionOption);
                    newQuizQuestionOptions.set(i, newQuizQuestionOption);
                }
                else
                {
                    throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
                }
            }
            return newQuizQuestionOptions;
        }
        catch (DataAccessException ex)
        {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    @Override
    public QuizQuestionOption getQuizQuestionOptionByQuizQuestionOptionId(Long quizQuestionId) throws QuizQuestionOptionNotFoundException
    {
        QuizQuestionOption quizQuestion = newQuizQuestionOptionRepository.findById(quizQuestionId).orElse(null);

        if (quizQuestion != null)
        {
            return quizQuestion;
        }
        else
        {
            throw new QuizQuestionOptionNotFoundException("QuizQuestionOption with ID: " + quizQuestionId + " does not exist!");
        }
    }

    @Override
    public QuizQuestionOption getQuizQuestionOptionByLeftContent(String leftContent) throws QuizQuestionOptionNotFoundException
    {
        QuizQuestionOption quizQuestion = newQuizQuestionOptionRepository.findByLeftContent(leftContent).orElse(null);

        if (quizQuestion != null)
        {
            return quizQuestion;
        }
        else
        {
            throw new QuizQuestionOptionNotFoundException("QuizQuestionOption with LeftContent: " + leftContent + " does not exist!");
        }
    }

    @Override
    public QuizQuestionOption getQuizQuestionOptionByRightContent(String rightContent) throws QuizQuestionOptionNotFoundException
    {
        QuizQuestionOption quizQuestion = newQuizQuestionOptionRepository.findByRightContent(rightContent).orElse(null);

        if (quizQuestion != null)
        {
            return quizQuestion;
        }
        else
        {
            throw new QuizQuestionOptionNotFoundException("QuizQuestionOption with RightContent: " + rightContent + " does not exist!");
        }
    }

    @Override
    public List<QuizQuestionOption> getAllQuizQuestionOptions()
    {
        return newQuizQuestionOptionRepository.findAll();
    }
}
