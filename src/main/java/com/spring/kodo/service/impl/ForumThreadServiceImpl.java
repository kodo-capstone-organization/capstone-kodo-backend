package com.spring.kodo.service.impl;

import com.spring.kodo.entity.ForumCategory;
import com.spring.kodo.entity.ForumPost;
import com.spring.kodo.entity.ForumThread;
import com.spring.kodo.repository.ForumThreadRepository;
import com.spring.kodo.service.inter.ForumCategoryService;
import com.spring.kodo.service.inter.ForumPostService;
import com.spring.kodo.service.inter.ForumThreadService;
import com.spring.kodo.util.MessageFormatterUtil;
import com.spring.kodo.util.exception.ForumCategoryNotFoundException;
import com.spring.kodo.util.exception.ForumPostNotFoundException;
import com.spring.kodo.util.exception.ForumThreadNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class ForumThreadServiceImpl implements ForumThreadService
{
    @Autowired // With this annotation, we do not to populate ForumThreadRepository in this class' constructor
    private ForumThreadRepository forumThreadRepository;
    @Autowired
    private ForumCategoryService forumCategoryService;
    @Autowired
    private ForumPostService forumPostService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ForumThreadServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public ForumThread createNewForumThread(ForumThread newForumThread) throws InputDataValidationException
    {
        Set<ConstraintViolation<ForumThread>> constraintViolations = validator.validate(newForumThread);
        if (constraintViolations.isEmpty())
        {
            return forumThreadRepository.saveAndFlush(newForumThread);
        }
        else
        {
            throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public ForumThread getForumThreadByForumThreadId(Long forumThreadId) throws ForumThreadNotFoundException
    {
        ForumThread forumThread = forumThreadRepository.findById(forumThreadId).orElse(null);

        if (forumThread != null)
        {
            return forumThread;
        }
        else
        {
            throw new ForumThreadNotFoundException("Forum Thread with ID: " + forumThreadId + " does not exist!");
        }
    }

    @Override
    public ForumThread getForumThreadByName(String name) throws ForumThreadNotFoundException
    {
        ForumThread forumThread = forumThreadRepository.findByName(name).orElse(null);

        if (forumThread != null)
        {
            return forumThread;
        }
        else
        {
            throw new ForumThreadNotFoundException("Forum Thread with name: " + name + " does not exist!");
        }
    }

    @Override
    public List<ForumThread> getAllForumThreadsOfAForumCategory(Long forumCategoryId) throws ForumCategoryNotFoundException
    {
        ForumCategory forumCategory = forumCategoryService.getForumCategoryByForumCategoryId(forumCategoryId);
        return forumCategory.getForumThreads();
    }

    //only updating attributes, not relationships
    @Override
    public ForumThread updateForumThread(Long forumThreadId, ForumThread updatedForumThread) throws ForumThreadNotFoundException
    {
        ForumThread forumThreadToUpdate = forumThreadRepository.findById(forumThreadId).orElse(null);

        if (forumThreadToUpdate != null)
        {
            forumThreadToUpdate.setDescription(updatedForumThread.getDescription());
            return forumThreadToUpdate;
        }
        else
        {
            throw new ForumThreadNotFoundException("Forum Thread with ID: " + forumThreadId + " does not exist!");
        }
    }

    @Override
    public Boolean deleteForumThread(Long forumThreadId) throws ForumThreadNotFoundException
    {
        ForumThread forumThreadToDelete = forumThreadRepository.findById(forumThreadId).orElse(null);

        if (forumThreadToDelete != null)
        {
            for (ForumPost post : forumThreadToDelete.getForumPosts())
            {
                try
                {
                    forumPostService.deleteForumPost(post.getForumPostId());
                }
                catch (ForumPostNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
            forumThreadRepository.deleteById(forumThreadId);
            return true;
        }
        else
        {
            throw new ForumThreadNotFoundException("Forum Thread with ID: " + forumThreadId + " does not exist!");
        }
    }
}
