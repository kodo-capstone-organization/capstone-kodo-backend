package com.spring.kodo.service.impl;

import com.spring.kodo.entity.EnrolledContent;
import com.spring.kodo.entity.EnrolledLesson;
import com.spring.kodo.entity.Lesson;
import com.spring.kodo.repository.EnrolledLessonRepository;
import com.spring.kodo.service.inter.EnrolledContentService;
import com.spring.kodo.service.inter.EnrolledLessonService;
import com.spring.kodo.service.inter.LessonService;
import com.spring.kodo.util.FormatterUtil;
import com.spring.kodo.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class EnrolledLessonServiceImpl implements EnrolledLessonService
{
    @Autowired
    private EnrolledLessonRepository enrolledLessonRepository;

    @Autowired
    private EnrolledContentService enrolledContentService;

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
    public EnrolledLesson createNewEnrolledLesson(Long parentLessonId) throws InputDataValidationException, UnknownPersistenceException, CreateNewEnrolledLessonException, LessonNotFoundException
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
                    throw new CreateNewEnrolledLessonException("EnrolledLesson ID cannot be null");
                }
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
    public EnrolledLesson getEnrolledLessonByEnrolledLessonId(Long enrolledLessonId) throws EnrolledLessonNotFoundException
    {
        EnrolledLesson enrolledLesson = enrolledLessonRepository.findById(enrolledLessonId).orElse(null);

        if (enrolledLesson != null)
        {
            return enrolledLesson;
        }
        else
        {
            throw new EnrolledLessonNotFoundException("EnrolledLesson with ID: " + enrolledLessonId + " does not exist!");
        }
    }

    @Override
    public EnrolledLesson getEnrolledLessonByEnrolledContentId(Long enrolledContentId) throws EnrolledLessonNotFoundException
    {
        EnrolledLesson enrolledLesson = enrolledLessonRepository.findByEnrolledContentId(enrolledContentId).orElse(null);

        if (enrolledLesson != null)
        {
            return enrolledLesson;
        }
        else
        {
            throw new EnrolledLessonNotFoundException("EnrolledLesson with EnrolledContent ID: " + enrolledContentId + " does not exist!");
        }
    }

    @Override
    public EnrolledLesson getEnrolledLessonByStudentIdAndLessonId(Long studentId, Long lessonId) throws EnrolledLessonNotFoundException
    {
        EnrolledLesson enrolledLesson = enrolledLessonRepository.findByStudentIdAndLessonId(studentId, lessonId).orElse(null);

        if (enrolledLesson != null)
        {
            return enrolledLesson;
        }
        else
        {
            throw new EnrolledLessonNotFoundException("EnrolledLesson with Account ID: " + studentId + " and Lesson ID: " + lessonId + " does not exist!");
        }
    }

    @Override
    public List<EnrolledLesson> getAllEnrolledLessons()
    {
        return enrolledLessonRepository.findAll();
    }

    @Override
    public List<EnrolledLesson> getAllEnrolledLessonsByParentLessonId(Long parentLessonId)
    {
        return enrolledLessonRepository.findAllEnrolledLessonsByParentLessonId(parentLessonId);
    }

    @Override
    public EnrolledLesson addEnrolledContentToEnrolledLesson(EnrolledLesson enrolledLesson, EnrolledContent enrolledContent) throws UpdateEnrolledLessonException, EnrolledLessonNotFoundException, EnrolledContentNotFoundException
    {
        if (enrolledLesson != null)
        {
            if (enrolledLesson.getEnrolledLessonId() != null)
            {
                enrolledLesson = getEnrolledLessonByEnrolledLessonId(enrolledLesson.getEnrolledLessonId());
                if (enrolledContent != null)
                {
                    if (enrolledContent.getEnrolledContentId() != null)
                    {
                        enrolledContent = enrolledContentService.getEnrolledContentByEnrolledContentId(enrolledContent.getEnrolledContentId());

                        if (!enrolledLesson.getEnrolledContents().contains(enrolledContent))
                        {
                            enrolledLesson.getEnrolledContents().add(enrolledContent);

                            enrolledLessonRepository.save(enrolledLesson);
                            return enrolledLesson;
                        }
                        else
                        {
                            throw new UpdateEnrolledLessonException("EnrolledLesson with ID " + enrolledLesson.getEnrolledLessonId() + " already contains EnrolledContent with ID " + enrolledContent.getEnrolledContentId());
                        }
                    }
                    else
                    {
                        throw new UpdateEnrolledLessonException("EnrolledContent ID cannot be null");
                    }
                }
                else
                {
                    throw new UpdateEnrolledLessonException("EnrolledContent cannot be null");
                }
            }
            else
            {
                throw new UpdateEnrolledLessonException("EnrolledLesson ID cannot be null");
            }
        }
        else
        {
            throw new UpdateEnrolledLessonException("EnrolledLesson cannot be null");
        }
    }

    @Override
    public EnrolledLesson checkDateTimeOfCompletionOfEnrolledLessonByEnrolledContentId(Long enrolledContentId) throws EnrolledLessonNotFoundException
    {
        EnrolledLesson enrolledLesson = getEnrolledLessonByEnrolledContentId(enrolledContentId);

        boolean setDateTimeOfCompletion = true;

        for (EnrolledContent enrolledContent : enrolledLesson.getEnrolledContents())
        {
            if (enrolledContent.getDateTimeOfCompletion() == null)
            {
                setDateTimeOfCompletion = false;
                break;
            }
        }

        if (setDateTimeOfCompletion)
        {
            enrolledLesson.setDateTimeOfCompletion(LocalDateTime.now());
        }
        else
        {
            enrolledLesson.setDateTimeOfCompletion(null);
        }

        enrolledLessonRepository.saveAndFlush(enrolledLesson);
        return enrolledLesson;
    }
}
