package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Content;
import com.spring.kodo.repository.ContentRepository;
import com.spring.kodo.service.ContentService;
import com.spring.kodo.util.MessageFormatterUtil;
import com.spring.kodo.util.exception.ContentNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.UnknownPersistenceException;
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
public class ContentServiceImpl implements ContentService
{
    @Autowired
    private ContentRepository contentRepository;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ContentServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Content createNewContent(Content content) throws InputDataValidationException, UnknownPersistenceException {

        try
        {
            Set<ConstraintViolation<Content>> constraintViolations = validator.validate(content);

            if(constraintViolations.isEmpty())
            {
                return contentRepository.saveAndFlush(content);
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
    public Content getContentByContentId(Long contentId) throws ContentNotFoundException
    {
        Content content = contentRepository.findById(contentId).orElse(null);

        if (content != null)
        {
            return content;
        }
        else
        {
            throw new ContentNotFoundException("Content with ID: " + contentId + " does not exist!");
        }
    }

    @Override
    public Content getContentByName(String name) throws ContentNotFoundException
    {
        Content content = contentRepository.findByName(name).orElse(null);

        if (content != null)
        {
            return content;
        }
        else
        {
            throw new ContentNotFoundException("Content with Name: " + name + " does not exist!");
        }
    }

    @Override
    public List<Content> getAllContents()
    {
        return contentRepository.findAll();
    }
}
