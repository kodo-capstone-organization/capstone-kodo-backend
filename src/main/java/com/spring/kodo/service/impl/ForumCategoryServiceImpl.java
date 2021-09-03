package com.spring.kodo.service.impl;

import com.spring.kodo.entity.ForumCategory;
import com.spring.kodo.entity.ForumThread;
import com.spring.kodo.repository.ForumCategoryRepository;
import com.spring.kodo.service.inter.ForumCategoryService;
import com.spring.kodo.service.inter.ForumThreadService;
import com.spring.kodo.util.MessageFormatterUtil;
import com.spring.kodo.util.exception.ForumCategoryNotFoundException;
import com.spring.kodo.util.exception.ForumThreadNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Service
public class ForumCategoryServiceImpl implements ForumCategoryService
{
    @Autowired // With this annotation, we do not to populate ForumCategoryRepository in this class' constructor
    private ForumCategoryRepository forumCategoryRepository;
    @Autowired
    private ForumThreadService forumThreadService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ForumCategoryServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public ForumCategory createNewForumCategory(ForumCategory newForumCategory) throws InputDataValidationException
    {
        Set<ConstraintViolation<ForumCategory>> constraintViolations = validator.validate(newForumCategory);
        if (constraintViolations.isEmpty())
        {
            return forumCategoryRepository.saveAndFlush(newForumCategory);
        }
        else
        {
            throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public ForumCategory getForumCategoryByForumCategoryId(Long forumCategoryId) throws ForumCategoryNotFoundException
    {
        ForumCategory forumCategory = forumCategoryRepository.findById(forumCategoryId).orElse(null);

        if (forumCategory != null)
        {
            return forumCategory;
        }
        else
        {
            throw new ForumCategoryNotFoundException("Forum Category with ID: " + forumCategoryId + " does not exist!");
        }
    }

    @Override
    public ForumCategory getForumCategoryByName(String name) throws ForumCategoryNotFoundException
    {
        ForumCategory forumCategory = forumCategoryRepository.findByName(name).orElse(null);

        if (forumCategory != null)
        {
            return forumCategory;
        }
        else
        {
            throw new ForumCategoryNotFoundException("Forum Category with Name: " + name + " does not exist!");
        }
    }

    //only updating attributes, not relationships
    @Override
    public ForumCategory updateForumCategory(Long forumCategoryId, ForumCategory updatedForumCategory) throws ForumCategoryNotFoundException
    {
        ForumCategory forumCategoryToUpdate = forumCategoryRepository.findById(forumCategoryId).orElse(null);

        if (forumCategoryToUpdate != null)
        {
            forumCategoryToUpdate.setName(updatedForumCategory.getName());
            forumCategoryToUpdate.setDescription(updatedForumCategory.getDescription());
            return forumCategoryToUpdate;
        }
        else
        {
            throw new ForumCategoryNotFoundException("Forum Category with ID: " + forumCategoryId + " does not exist!");
        }
    }

    @Override
    public Boolean deleteForumCategory(Long forumCategoryId) throws ForumCategoryNotFoundException
    {
        ForumCategory forumCategoryToDelete = forumCategoryRepository.findById(forumCategoryId).orElse(null);

        if (forumCategoryToDelete != null)
        {
            for (ForumThread thread : forumCategoryToDelete.getForumThreads())
            {
                try
                {
                    forumThreadService.deleteForumThread(thread.getForumThreadId());
                }
                catch (ForumThreadNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
            forumCategoryRepository.deleteById(forumCategoryId);
            return true;
        }
        else
        {
            throw new ForumCategoryNotFoundException("Forum Category with ID: " + forumCategoryId + " does not exist!");
        }
    }
}