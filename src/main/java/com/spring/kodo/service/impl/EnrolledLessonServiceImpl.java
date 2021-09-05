package com.spring.kodo.service.impl;

import com.spring.kodo.entity.EnrolledLesson;
import com.spring.kodo.entity.Lesson;
import com.spring.kodo.repository.EnrolledLessonRepository;
import com.spring.kodo.service.inter.EnrolledLessonService;
import com.spring.kodo.service.inter.LessonService;
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
public class EnrolledLessonServiceImpl implements EnrolledLessonService
{
    @Autowired
    private EnrolledLessonRepository enrolledLessonRepository;

    @Autowired
    private LessonService lessonService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public EnrolledLessonServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public EnrolledLesson createNewEnrolledLesson(Long parentLessonId) throws InputDataValidationException, UnknownPersistenceException, CreateNewCompletedLessonException, LessonNotFoundException
    {
        try
        {
            EnrolledLesson newEnrolledLesson = new EnrolledLesson();

            Set<ConstraintViolation<EnrolledLesson>> constraintViolations = validator.validate(newEnrolledLesson);
            if (constraintViolations.isEmpty())
            {
                if (parentLessonId != null)
                {
                    Lesson parentLesson = lessonService.getLessonByLessonId(parentLessonId);
                    newEnrolledLesson.setParentLesson(parentLesson);

                    enrolledLessonRepository.saveAndFlush(newEnrolledLesson);
                    return newEnrolledLesson;
                }
                else
                {
                    throw new CreateNewCompletedLessonException("EnrolledLesson ID cannot be null");
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
    public EnrolledLesson getEnrolledLessonByEnrolledLessonId(Long enrolledLessonId) throws CompletedLessonNotFoundException
    {
        EnrolledLesson enrolledLesson = enrolledLessonRepository.findById(enrolledLessonId).orElse(null);

        if (enrolledLesson != null)
        {
            return enrolledLesson;
        }
        else
        {
            throw new CompletedLessonNotFoundException("EnrolledLesson with ID: " + enrolledLessonId + " does not exist!");
        }
    }

    @Override
    public List<EnrolledLesson> getAllEnrolledLessons()
    {
        return enrolledLessonRepository.findAll();
    }
}
