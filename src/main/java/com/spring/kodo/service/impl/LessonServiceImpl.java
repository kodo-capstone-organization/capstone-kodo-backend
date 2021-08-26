package com.spring.kodo.service.impl;

import com.spring.kodo.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spring.kodo.service.LessonService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import com.spring.kodo.entity.Lesson;
import com.spring.kodo.util.MessageFormatterUtil;
import com.spring.kodo.util.exception.*;

@Service
public class LessonServiceImpl implements LessonService
{

    @Autowired // With this annotation, we do not to populate LessonRepository in this class' constructor
    private LessonRepository lessonRepository;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public LessonServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Lesson createNewLesson(Lesson newLesson) throws InputDataValidationException
    {
        Set<ConstraintViolation<Lesson>> constraintViolations = validator.validate(newLesson);
        if (constraintViolations.isEmpty())
        {
            return lessonRepository.saveAndFlush(newLesson);
        }
        else
        {
            throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public Lesson getLessonByLessonId(Long lessonId) throws LessonNotFoundException
    {
        Lesson lesson = lessonRepository.findById(lessonId).orElse(null);

        if (lesson != null)
        {
            return lesson;
        }
        else
        {
            throw new LessonNotFoundException("Lesson with ID: " + lessonId + " does not exist!");
        }
    }

    @Override
    public Lesson getLessonByName(String name) throws LessonNotFoundException
    {
        Lesson lesson = lessonRepository.findByName(name).orElse(null);

        if (lesson != null)
        {
            return lesson;
        }
        else
        {
            throw new LessonNotFoundException("Lesson with Name: " + name + " does not exist!");
        }
    }

    @Override
    public Lesson updateLesson(Long lessonId, Lesson updatedLesson) throws LessonNotFoundException
    {
        Lesson lessonToUpdate = lessonRepository.findById(lessonId).orElse(null);

        if (lessonToUpdate != null)
        {
            lessonToUpdate.setName(updatedLesson.getName());
            lessonToUpdate.setDescription(updatedLesson.getDescription());
            lessonToUpdate.setSequence(updatedLesson.getSequence());
            lessonToUpdate.setContents(updatedLesson.getContents());
            lessonRepository.saveAndFlush(lessonToUpdate);
            return lessonToUpdate;
        }
        else
        {
            throw new LessonNotFoundException("Lesson with ID: " + lessonId + " does not exist!");
        }
    }

    @Override
    public Lesson deleteLesson(Long lessonId) throws LessonNotFoundException
    {
        Lesson lessonToDelete = lessonRepository.findById(lessonId).orElse(null);

        if (lessonToDelete != null)
        {
            lessonRepository.deleteById(lessonId);
            return lessonToDelete;
        }
        else
        {
            throw new LessonNotFoundException("Lesson with ID: " + lessonId + " does not exist!");
        }
    }
}
