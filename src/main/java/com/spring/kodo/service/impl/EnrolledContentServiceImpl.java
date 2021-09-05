package com.spring.kodo.service.impl;

import com.spring.kodo.entity.EnrolledContent;
import com.spring.kodo.entity.Content;
import com.spring.kodo.repository.EnrolledContentRepository;
import com.spring.kodo.service.inter.EnrolledContentService;
import com.spring.kodo.service.inter.ContentService;
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
public class EnrolledContentServiceImpl implements EnrolledContentService
{
    @Autowired
    private EnrolledContentRepository enrolledContentRepository;

    @Autowired
    private ContentService lessonService;

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
                    Content parentContent = lessonService.getContentByContentId(parentContentId);
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
                throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
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
    public List<EnrolledContent> getAllEnrolledContents()
    {
        return enrolledContentRepository.findAll();
    }
}
