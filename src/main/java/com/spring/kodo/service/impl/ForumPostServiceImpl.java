package com.spring.kodo.service.impl;

import com.spring.kodo.entity.ForumPost;
import com.spring.kodo.repository.ForumPostRepository;
import com.spring.kodo.service.ForumPostService;
import com.spring.kodo.util.MessageFormatterUtil;
import com.spring.kodo.util.exception.ForumPostNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ForumPostServiceImpl implements ForumPostService {

    @Autowired // With this annotation, we do not to populate ForumPostRepository in this class' constructor
    private ForumPostRepository forumPostRepository;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ForumPostServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public ForumPost createNewForumPost (ForumPost newForumPost) throws InputDataValidationException
    {
        Set<ConstraintViolation<ForumPost>> constraintViolations = validator.validate(newForumPost);
        if (constraintViolations.isEmpty())
        {
            return forumPostRepository.saveAndFlush(newForumPost);
        }
        else
        {
            throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public ForumPost getForumPostByForumPostId(Long forumPostId) throws ForumPostNotFoundException
    {
        ForumPost forumPost = forumPostRepository.findById(forumPostId).orElse(null);

        if (forumPost != null)
        {
            return forumPost;
        }
        else
        {
            throw new ForumPostNotFoundException("Forum Post with ID: " + forumPostId + " does not exist!");
        }
    }

    //only updating attributes, not relationships
    @Override
    public ForumPost updateForumPost(Long forumPostId, ForumPost updatedForumPost) throws ForumPostNotFoundException
    {
        ForumPost forumPostToUpdate  = forumPostRepository.findById(forumPostId).orElse(null);

        if (forumPostToUpdate != null)
        {
            forumPostToUpdate.setMessage(forumPostToUpdate.getMessage());
            return forumPostToUpdate;
        }
        else
        {
            throw new ForumPostNotFoundException("Forum Post with ID: " + forumPostId + " does not exist!");
        }
    }

    @Override
    public Boolean deleteForumPost (Long forumPostId) throws ForumPostNotFoundException
    {
        ForumPost forumPostToDelete = forumPostRepository.findById(forumPostId).orElse(null);

        if (forumPostToDelete != null)
        {
            forumPostRepository.deleteById(forumPostId);
            return true;
        }
        else
        {
            throw new ForumPostNotFoundException("Forum Post with ID: " + forumPostId + " does not exist!");
        }
    }
}
