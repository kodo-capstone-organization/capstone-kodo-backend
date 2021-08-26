package com.spring.kodo.service.impl;

import com.spring.kodo.entity.QuizQuestionOption;
import com.spring.kodo.repository.QuizQuestionOptionRepository;
import com.spring.kodo.service.QuizQuestionOptionService;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.QuizQuestionOptionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

@Service
public class QuizQuestionOptionServiceImpl implements QuizQuestionOptionService
{
    @Autowired
    private QuizQuestionOptionRepository quizQuestionRepository;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public QuizQuestionOptionServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public QuizQuestionOption createNewQuizQuestionOption(QuizQuestionOption quizQuestionOption) throws InputDataValidationException
    {
        return null;
    }

    @Override
    public QuizQuestionOption getQuizQuestionOptionByQuizQuestionOptionId(Long quizQuestionId) throws QuizQuestionOptionNotFoundException
    {
        QuizQuestionOption quizQuestion = quizQuestionRepository.findById(quizQuestionId).orElse(null);

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
        QuizQuestionOption quizQuestion = quizQuestionRepository.findByLeftContent(leftContent).orElse(null);

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
        QuizQuestionOption quizQuestion = quizQuestionRepository.findByRightContent(rightContent).orElse(null);

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
        return quizQuestionRepository.findAll();
    }
}
