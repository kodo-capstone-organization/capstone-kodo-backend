package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Content;
import com.spring.kodo.entity.Lesson;
import com.spring.kodo.entity.Multimedia;
import com.spring.kodo.repository.MultimediaRepository;
import com.spring.kodo.service.ContentService;
import com.spring.kodo.service.FileService;
import com.spring.kodo.service.LessonService;
import com.spring.kodo.service.MultimediaService;
import com.spring.kodo.util.MessageFormatterUtil;
import com.spring.kodo.util.enumeration.MultimediaType;
import com.spring.kodo.util.exception.LessonNotFoundException;
import com.spring.kodo.util.exception.MultimediaNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.UnknownPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class MultimediaServiceImpl implements MultimediaService
{
    @Autowired
    private MultimediaRepository multimediaRepository;

    @Autowired
    private ContentService contentService;

    @Autowired
    private FileService fileService;

    @Autowired
    private LessonService lessonService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public MultimediaServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Multimedia createNewMultimedia(Multimedia multimedia) throws InputDataValidationException, UnknownPersistenceException
    {
        // Multimedia inherits from content
        // Constraint violation checks on child also checks parent variables
        Set<ConstraintViolation<Content>> constraintViolations = validator.validate(multimedia);
        if(constraintViolations.isEmpty())
        {
            return (Multimedia) contentService.createNewContent(multimedia);
        }
        else
        {
            throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public Multimedia getMultimediaByMultimediaId(Long multimediaId) throws MultimediaNotFoundException
    {
        Multimedia multimedia = multimediaRepository.findById(multimediaId).orElse(null);

        if (multimedia != null)
        {
            return multimedia;
        }
        else
        {
            throw new MultimediaNotFoundException("Multimedia with ID: " + multimediaId + " does not exist!");
        }
    }

    @Override
    public Multimedia getMultimediaByUrl(String url) throws MultimediaNotFoundException
    {
        Multimedia multimedia = multimediaRepository.findByUrl(url).orElse(null);

        if (multimedia != null)
        {
            return multimedia;
        }
        else
        {
            throw new MultimediaNotFoundException("Multimedia with Url: " + url + " does not exist!");
        }
    }

    @Override
    public Multimedia getMultimediaByType(MultimediaType multimediaType) throws MultimediaNotFoundException
    {
        Multimedia multimedia = multimediaRepository.findByType(multimediaType).orElse(null);

        if (multimedia != null)
        {
            return multimedia;
        }
        else
        {
            throw new MultimediaNotFoundException("Multimedia with MultimediaType: " + multimediaType + " does not exist!");
        }
    }

    @Override
    public List<Multimedia> getAllMultimedias()
    {
        return multimediaRepository.findAll();
    }

    @Override
    public void deleteMultimedia(Long contentId) throws MultimediaNotFoundException
    {
        Multimedia multimediaToRemove = getMultimediaByMultimediaId(contentId);
        try
        {
            Lesson linkedLesson = lessonService.getLessonByLessonId(multimediaToRemove.getLesson().getLessonId());
            linkedLesson.getContents().remove(multimediaToRemove);
        }
        catch (LessonNotFoundException e)
        {
            // If no lesson found, all the more we should delete this multimedia
        }

        // Delete from cloud
        fileService.delete(multimediaToRemove.getUrlFilename());
        multimediaRepository.delete(multimediaToRemove);
    }
}
