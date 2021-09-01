package com.spring.kodo.service.impl;

import com.spring.kodo.entity.CompletedLesson;
import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.Lesson;
import com.spring.kodo.repository.CompletedLessonRepository;
import com.spring.kodo.service.CompletedLessonService;
import com.spring.kodo.service.LessonService;
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
public class CompletedLessonServiceImpl implements CompletedLessonService
{
    @Autowired
    private CompletedLessonRepository completedLessonRepository;

    @Autowired
    private LessonService lessonService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CompletedLessonServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public CompletedLesson createNewCompletedLesson(Long parentLessonId) throws InputDataValidationException, UnknownPersistenceException, CreateNewCompletedLessonException, LessonNotFoundException
    {
        try
        {
            CompletedLesson newCompletedLesson = new CompletedLesson();

            Set<ConstraintViolation<CompletedLesson>> constraintViolations = validator.validate(newCompletedLesson);
            if (constraintViolations.isEmpty())
            {
                if (parentLessonId != null)
                {
                    Lesson parentLesson = lessonService.getLessonByLessonId(parentLessonId);
                    newCompletedLesson.setParentLesson(parentLesson);

                    completedLessonRepository.saveAndFlush(newCompletedLesson);
                    return newCompletedLesson;
                }
                else
                {
                    throw new CreateNewCompletedLessonException("CompletedLessonId cannot be null");
                }
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
