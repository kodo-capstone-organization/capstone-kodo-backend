package com.spring.kodo.service.impl;

import com.spring.kodo.entity.CompletedLesson;
import com.spring.kodo.repository.CompletedLessonRepository;
import com.spring.kodo.service.CompletedLessonService;
import com.spring.kodo.util.exception.CompletedLessonNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

@Service
public class CompletedLessonServiceImpl implements CompletedLessonService
{
    @Autowired
    private CompletedLessonRepository completedLessonRepository;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CompletedLessonServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public CompletedLesson createNewCompletedLesson(CompletedLesson completedLesson) throws InputDataValidationException
    {
        return null;
    }

    @Override
    public CompletedLesson getCompletedLessonByCompletedLessonId(Long completedLessonId) throws CompletedLessonNotFoundException
    {
        CompletedLesson completedLesson = completedLessonRepository.findById(completedLessonId).orElse(null);

        if (completedLesson != null)
        {
            return completedLesson;
        }
        else
        {
            throw new CompletedLessonNotFoundException("CompletedLesson with ID: " + completedLessonId + " does not exist!");
        }
    }

    @Override
    public List<CompletedLesson> getAllCompletedLessons()
    {
        return completedLessonRepository.findAll();
    }
}
