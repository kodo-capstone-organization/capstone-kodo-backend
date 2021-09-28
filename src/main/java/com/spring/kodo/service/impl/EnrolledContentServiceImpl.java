package com.spring.kodo.service.impl;

import com.spring.kodo.entity.*;
import com.spring.kodo.repository.EnrolledContentRepository;
import com.spring.kodo.service.inter.*;
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
public class EnrolledContentServiceImpl implements EnrolledContentService
{
    @Autowired
    private EnrolledContentRepository enrolledContentRepository;

    @Autowired
    private EnrolledCourseService enrolledCourseService;

    @Autowired
    private EnrolledLessonService enrolledLessonService;

    @Autowired
    private StudentAttemptService studentAttemptService;

    @Autowired
    private ContentService contentService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public EnrolledContentServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public EnrolledContent createNewEnrolledContent(Long parentContentId) throws InputDataValidationException, UnknownPersistenceException, CreateNewEnrolledContentException, ContentNotFoundException
    {
        try
        {
            EnrolledContent newEnrolledContent = new EnrolledContent();

            Set<ConstraintViolation<EnrolledContent>> constraintViolations = validator.validate(newEnrolledContent);
            if (constraintViolations.isEmpty())
            {
                if (parentContentId != null)
                {
                    Content parentContent = contentService.getContentByContentId(parentContentId);
                    newEnrolledContent.setParentContent(parentContent);

                    enrolledContentRepository.saveAndFlush(newEnrolledContent);
                    return newEnrolledContent;
                }
                else
                {
                    throw new CreateNewEnrolledContentException("EnrolledContent ID cannot be null");
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
    public EnrolledContent getEnrolledContentByEnrolledContentId(Long enrolledContentId) throws EnrolledContentNotFoundException
    {
        EnrolledContent enrolledContent = enrolledContentRepository.findById(enrolledContentId).orElse(null);

        if (enrolledContent != null)
        {
            return enrolledContent;
        }
        else
        {
            throw new EnrolledContentNotFoundException("EnrolledContent with ID: " + enrolledContentId + " does not exist!");
        }
    }

    @Override
    public EnrolledContent getEnrolledContentByAccountIdAndContentId(Long accountId, Long contentId) throws EnrolledContentNotFoundException
    {
        EnrolledContent enrolledContent = enrolledContentRepository.findByAccountIdAndContentId(accountId, contentId).orElse(null);

        if (enrolledContent != null)
        {
            return enrolledContent;
        }
        else
        {
            throw new EnrolledContentNotFoundException("EnrolledContent with Account ID: " + accountId + " and Content ID: " + contentId + " does not exist!");
        }
    }

    @Override
    public List<EnrolledContent> getAllEnrolledContents()
    {
        return enrolledContentRepository.findAll();
    }

    @Override
    public EnrolledContent addStudentAttemptToEnrolledContent(EnrolledContent enrolledContent, StudentAttempt studentAttempt) throws UpdateEnrolledContentException, EnrolledContentNotFoundException, StudentAttemptNotFoundException
    {
        if (enrolledContent != null)
        {
            if (enrolledContent.getEnrolledContentId() != null)
            {
                enrolledContent = getEnrolledContentByEnrolledContentId(enrolledContent.getEnrolledContentId());
                if (studentAttempt != null)
                {
                    if (studentAttempt.getStudentAttemptId() != null)
                    {
                        studentAttempt = studentAttemptService.getStudentAttemptByStudentAttemptId(studentAttempt.getStudentAttemptId());

                        if (!enrolledContent.getStudentAttempts().contains(studentAttempt))
                        {
                            if (enrolledContent.getStudentAttempts().size() < studentAttempt.getQuiz().getMaxAttemptsPerStudent())
                            {
                                enrolledContent.getStudentAttempts().add(studentAttempt);

                                enrolledContentRepository.save(enrolledContent);
                                return enrolledContent;
                            }
                            else
                            {
                                studentAttemptService.deleteStudentAttemptByStudentAttemptId(studentAttempt.getStudentAttemptId()); // Needs work
                                throw new UpdateEnrolledContentException("This enrolledContent has ran out of attempts for quiz: " + studentAttempt.getQuiz().getName() + ". Attempt is thus deleted");
                            }
                        }
                        else
                        {
                            throw new UpdateEnrolledContentException("EnrolledContent with ID " + enrolledContent.getEnrolledContentId() + " already contains StudentAttempt with ID " + studentAttempt.getStudentAttemptId());
                        }
                    }
                    else
                    {
                        throw new UpdateEnrolledContentException("StudentAttempt ID cannot be null");
                    }
                }
                else
                {
                    throw new UpdateEnrolledContentException("StudentAttempt cannot be null");
                }
            }
            else
            {
                throw new UpdateEnrolledContentException("EnrolledContent ID cannot be null");
            }
        }
        else
        {
            throw new UpdateEnrolledContentException("EnrolledContent cannot be null");
        }
    }

    @Override
    public EnrolledContent setDateTimeOfCompletionOfEnrolledContentByEnrolledContentId(boolean complete, Long enrolledContentId) throws EnrolledContentNotFoundException, EnrolledLessonNotFoundException, EnrolledCourseNotFoundException
    {
        EnrolledContent enrolledContent = getEnrolledContentByEnrolledContentId(enrolledContentId);

        enrolledContent = setDateTimeOfCompletionOfEnrolledContentByEnrolledContent(complete, enrolledContent, LocalDateTime.now());

        return enrolledContent;
    }

    @Override
    public EnrolledContent setDateTimeOfCompletionOfEnrolledContentByAccountIdAndContentId(boolean complete, Long accountId, Long contentId) throws EnrolledContentNotFoundException, EnrolledLessonNotFoundException, EnrolledCourseNotFoundException
    {
        EnrolledContent enrolledContent = getEnrolledContentByAccountIdAndContentId(accountId, contentId);

        enrolledContent = setDateTimeOfCompletionOfEnrolledContentByEnrolledContent(complete, enrolledContent, LocalDateTime.now());

        return enrolledContent;
    }

    @Override
    public EnrolledContent setFakeDateTimeOfCompletionOfEnrolledContentByEnrolledContentId(LocalDateTime dateTimeOfCompletion, Long enrolledContentId) throws EnrolledContentNotFoundException, EnrolledLessonNotFoundException, EnrolledCourseNotFoundException, InvalidDateTimeOfCompletionException
    {
        EnrolledCourse enrolledCourse = enrolledCourseService.getEnrolledCourseByEnrolledContentId(enrolledContentId);

        if (enrolledCourse.getParentCourse().getDateTimeOfCreation().isBefore(dateTimeOfCompletion))
        {
            EnrolledContent enrolledContent = getEnrolledContentByEnrolledContentId(enrolledContentId);

            enrolledContent = setDateTimeOfCompletionOfEnrolledContentByEnrolledContent(true, enrolledContent, dateTimeOfCompletion);

            return enrolledContent;
        }
        else
        {
            throw new InvalidDateTimeOfCompletionException("Input DateTimeOfCompletion is invalid. Course date of creation in " + enrolledCourse.getParentCourse().getDateTimeOfCreation() + " cannot be after " + dateTimeOfCompletion);
        }
    }

    private EnrolledContent setDateTimeOfCompletionOfEnrolledContentByEnrolledContent(boolean complete, EnrolledContent enrolledContent, LocalDateTime dateTimeOfCompletion) throws EnrolledLessonNotFoundException, EnrolledCourseNotFoundException
    {
        if (complete)
        {
            enrolledContent.setDateTimeOfCompletion(dateTimeOfCompletion);
            enrolledContentRepository.saveAndFlush(enrolledContent);
        }
        else
        {
            enrolledContent.setDateTimeOfCompletion(null);
        }

        EnrolledLesson enrolledLesson = enrolledLessonService.checkDateTimeOfCompletionOfEnrolledLessonByEnrolledContentId(enrolledContent.getEnrolledContentId(), dateTimeOfCompletion);
        EnrolledCourse enrolledCourse = enrolledCourseService.checkDateTimeOfCompletionOfEnrolledCourseByEnrolledLessonId(enrolledLesson.getEnrolledLessonId(), dateTimeOfCompletion);

        return enrolledContent;
    }
}
