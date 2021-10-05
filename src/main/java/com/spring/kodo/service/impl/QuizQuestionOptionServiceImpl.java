package com.spring.kodo.service.impl;

import com.spring.kodo.entity.QuizQuestionOption;
import com.spring.kodo.repository.QuizQuestionOptionRepository;
import com.spring.kodo.service.inter.QuizQuestionOptionService;
import com.spring.kodo.util.FormatterUtil;
import com.spring.kodo.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class QuizQuestionOptionServiceImpl implements QuizQuestionOptionService
{
    @Autowired
    private QuizQuestionOptionRepository quizQuestionOptionRepository;

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
                quizQuestionOptionRepository.saveAndFlush(newQuizQuestionOption);
                return newQuizQuestionOption;
            }
            else
            {
                throw new InputDataValidationException(FormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        catch (DataAccessException ex)
        {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    @Override
    public List<QuizQuestionOption> createNewQuizQuestionOptions(List<QuizQuestionOption> newQuizQuestionOptions) throws InputDataValidationException, UnknownPersistenceException, CreateNewQuizQuestionOptionException
    {
        try
        {
            boolean hasRightContent = newQuizQuestionOptions.get(0).getRightContent() != null;

            Set<String> leftContents = new HashSet<>();
            Set<String> rightContents = new HashSet<>();

            // Check if any options are the same
            for (QuizQuestionOption quizQuestionOption : newQuizQuestionOptions)
            {
                leftContents.add(quizQuestionOption.getLeftContent());
                rightContents.add(quizQuestionOption.getRightContent());
            }

            if ((!hasRightContent && leftContents.size() == newQuizQuestionOptions.size()) || (leftContents.size() == rightContents.size()))
            {
                // Create options
                for (int i = 0; i < newQuizQuestionOptions.size(); i++)
                {
                    QuizQuestionOption newQuizQuestionOption = newQuizQuestionOptions.get(i);
                    Set<ConstraintViolation<QuizQuestionOption>> constraintViolations = validator.validate(newQuizQuestionOption);
                    if (constraintViolations.isEmpty())
                    {
                        quizQuestionOptionRepository.saveAndFlush(newQuizQuestionOption);
                        newQuizQuestionOptions.set(i, newQuizQuestionOption);
                    }
                    else
                    {
                        throw new InputDataValidationException(FormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
                    }
                }
                return newQuizQuestionOptions;
            }
            else
            {
                throw new CreateNewQuizQuestionOptionException("Options should be unique");
            }
        }
        catch (DataAccessException ex)
        {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    @Override
    public QuizQuestionOption getQuizQuestionOptionByQuizQuestionOptionId(Long quizQuestionId) throws QuizQuestionOptionNotFoundException
    {
        QuizQuestionOption quizQuestion = quizQuestionOptionRepository.findById(quizQuestionId).orElse(null);

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
        QuizQuestionOption quizQuestion = quizQuestionOptionRepository.findByLeftContent(leftContent).orElse(null);

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
        QuizQuestionOption quizQuestion = quizQuestionOptionRepository.findByRightContent(rightContent).orElse(null);

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
        return quizQuestionOptionRepository.findAll();
    }

    @Override
    public QuizQuestionOption updateQuizQuestionOption(QuizQuestionOption quizQuestionOption) throws UpdateQuizQuestionOptionException, QuizQuestionOptionNotFoundException, InputDataValidationException
    {
        if (quizQuestionOption != null)
        {
            if (quizQuestionOption.getQuizQuestionOptionId() != null)
            {
                Set<ConstraintViolation<QuizQuestionOption>> constraintViolations = validator.validate(quizQuestionOption);

                if (constraintViolations.isEmpty())
                {
                    QuizQuestionOption quizQuestionOptionToUpdate = getQuizQuestionOptionByQuizQuestionOptionId(quizQuestionOption.getQuizQuestionOptionId());

                    quizQuestionOptionToUpdate.setCorrect(quizQuestionOption.getCorrect());
                    quizQuestionOptionToUpdate.setLeftContent(quizQuestionOption.getLeftContent());
                    quizQuestionOptionToUpdate.setRightContent(quizQuestionOption.getRightContent());

                    quizQuestionOptionRepository.saveAndFlush(quizQuestionOptionToUpdate);
                    return quizQuestionOptionToUpdate;
                }
                else
                {
                    throw new InputDataValidationException(FormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
                }
            }
            else
            {
                throw new UpdateQuizQuestionOptionException("QuizQuestionOption ID cannot be null");
            }
        }
        else
        {
            throw new UpdateQuizQuestionOptionException("QuizQuestionOption cannot be null");
        }
    }

    @Override
    public Boolean deleteQuizQuestionOptionByQuizQuestionOptionId(Long quizQuestionOptionId) throws DeleteQuizQuestionOptionException, QuizQuestionOptionNotFoundException
    {
        if (quizQuestionOptionId != null)
        {
            QuizQuestionOption quizQuestionOptionToDelete = getQuizQuestionOptionByQuizQuestionOptionId(quizQuestionOptionId);

            quizQuestionOptionRepository.delete(quizQuestionOptionToDelete);
            return true;
        }
        else
        {
            throw new DeleteQuizQuestionOptionException("QuizQuestionOption ID cannot be null");
        }
    }
}
