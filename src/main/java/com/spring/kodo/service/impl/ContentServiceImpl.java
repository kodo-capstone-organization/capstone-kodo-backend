package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Content;
import com.spring.kodo.repository.ContentRepository;
import com.spring.kodo.service.ContentService;
import com.spring.kodo.util.exception.ContentNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService
{
    @Autowired
    private ContentRepository courseRepository;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ContentServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Content createNewContent(Content content) throws InputDataValidationException
    {
        return null;
    }

    @Override
    public List<Content> getAllContents()
    {
        return courseRepository.findAll();
    }

    @Override
    public Content getContentByName(String name) throws ContentNotFoundException
    {
        Content course = courseRepository.findByName(name).orElse(null);

        if (course != null)
        {
            return course;
        }
        else
        {
            throw new ContentNotFoundException("Content with Name: " + name + " does not exist!");
        }
    }
}
