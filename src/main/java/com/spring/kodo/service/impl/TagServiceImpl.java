package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Tag;
import com.spring.kodo.repository.TagRepository;
import com.spring.kodo.service.inter.TagService;
import com.spring.kodo.util.MessageFormatterUtil;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.TagNameExistsException;
import com.spring.kodo.util.exception.TagNotFoundException;
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
public class TagServiceImpl implements TagService
{
    @Autowired // With this annotation, we do not to populate TagRepository in this class' constructor
    private TagRepository tagRepository;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public TagServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }


    @Override
    public Tag createNewTag(Tag newTag) throws TagNameExistsException, UnknownPersistenceException, InputDataValidationException
    {
        try
        {
            Set<ConstraintViolation<Tag>> constraintViolations = validator.validate(newTag);
            if (constraintViolations.isEmpty())
            {
                tagRepository.saveAndFlush(newTag);
                return newTag;
            }
            else
            {
                throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        catch (DataAccessException ex)
        {
            if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
            {
                throw new TagNameExistsException("Tag Name is already in use!");
            }
            else
            {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public Tag getTagByTagId(Long tagId) throws TagNotFoundException
    {
        Tag tag = tagRepository.findById(tagId).orElse(null);

        if (tag != null)
        {
            return tag;
        }
        else
        {
            throw new TagNotFoundException("Tag with ID: " + tagId + " does not exist!");
        }
    }

    @Override
    public Tag getTagByTitle(String tagTitle) throws TagNotFoundException
    {
        Tag tag = tagRepository.findByTitle(tagTitle).orElse(null);

        if (tag != null)
        {
            return tag;
        }
        else
        {
            throw new TagNotFoundException("Tag with Title: " + tagTitle + " does not exist!");
        }
    }


    @Override
    public Tag getTagByTitleOrCreateNew(String tagTitle) throws TagNameExistsException, UnknownPersistenceException, InputDataValidationException
    {
        Tag tag = null;
        try
        {
            tag = getTagByTitle(tagTitle);
        }
        catch (TagNotFoundException ex)
        {
            Tag newTag = new Tag(tagTitle);
            tag = createNewTag(newTag);
        }

        return tag;
    }

    @Override
    public List<Tag> getAllTags()
    {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> getTopRelevantTagsThroughFrequencyWithLimitByAccountId(Long accountId, Integer limit)
    {
        return tagRepository.findTopRelevantTagsThroughFrequencyWithLimitByAccountId(accountId, limit);
    }
}

