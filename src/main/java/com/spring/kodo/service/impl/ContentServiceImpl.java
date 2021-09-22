package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Content;
import com.spring.kodo.entity.Multimedia;
import com.spring.kodo.entity.Quiz;
import com.spring.kodo.repository.ContentRepository;
import com.spring.kodo.service.inter.ContentService;
import com.spring.kodo.service.inter.MultimediaService;
import com.spring.kodo.service.inter.QuizService;
import com.spring.kodo.util.exception.ContentNotFoundException;
import com.spring.kodo.util.exception.UpdateContentException;
import org.apache.http.MethodNotSupportedException;
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
    private ContentRepository contentRepository;

    @Autowired
    private QuizService quizService;

    @Autowired
    private MultimediaService multimediaService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ContentServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
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
