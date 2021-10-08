package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.ForumPost;
import com.spring.kodo.entity.ForumThread;
import com.spring.kodo.repository.ForumPostRepository;
import com.spring.kodo.service.inter.AccountService;
import com.spring.kodo.service.inter.ForumPostService;
import com.spring.kodo.service.inter.ForumThreadService;
import com.spring.kodo.util.FormatterUtil;
import com.spring.kodo.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ForumPostServiceImpl implements ForumPostService
{

    @Autowired // With this annotation, we do not to populate ForumPostRepository in this class' constructor
    private ForumPostRepository forumPostRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ForumThreadService forumThreadService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ForumPostServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public ForumPost createNewForumPost(ForumPost newForumPost, Long accountId) throws InputDataValidationException, UnknownPersistenceException, AccountNotFoundException
    {
        try
        {
            Set<ConstraintViolation<ForumPost>> constraintViolations = validator.validate(newForumPost);
            if (constraintViolations.isEmpty())
            {
                Account account = accountService.getAccountByAccountId(accountId);
                newForumPost.setAccount(account);
                forumPostRepository.saveAndFlush(newForumPost);
                return newForumPost;
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
    public ForumPost createNewForumPostReply(ForumPost newForumPostReply, Long accountId, Long parentForumPostId) throws UnknownPersistenceException, InputDataValidationException, AccountNotFoundException, ForumPostNotFoundException
    {
        try
        {
            Set<ConstraintViolation<ForumPost>> constraintViolations = validator.validate(newForumPostReply);
            if (constraintViolations.isEmpty())
            {
                Account account = accountService.getAccountByAccountId(accountId);
                newForumPostReply.setAccount(account);

                ForumPost parentForumPost = getForumPostByForumPostId(parentForumPostId);
                newForumPostReply.setParentForumPost(parentForumPost);

                forumPostRepository.saveAndFlush(newForumPostReply);
                forumPostRepository.saveAndFlush(parentForumPost);
                return newForumPostReply;
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

    @Override
    public List<ForumPost> getAllForumPosts()
    {
        return forumPostRepository.findAll();
    }

    @Override
    public List<ForumPost> getAllForumPostsOfAForumThread(Long forumThreadId) throws ForumThreadNotFoundException
    {
        ForumThread forumThread = forumThreadService.getForumThreadByForumThreadId(forumThreadId);
        return forumThread.getForumPosts();
    }

    @Override
    public List<ForumPost> getAllForumPostsByParentForumPostId(Long parentForumPostId)
    {
        return forumPostRepository.findAllByParentForumPostId(parentForumPostId);
    }

    //only updating attributes, not relationships
    @Override
    public ForumPost updateForumPost(ForumPost updatedForumPost) throws ForumPostNotFoundException, InputDataValidationException
    {
        if (updatedForumPost != null && updatedForumPost.getForumPostId() != null)
        {
            Set<ConstraintViolation<ForumPost>> constraintViolations = validator.validate(updatedForumPost);
            if (constraintViolations.isEmpty())
            {
                ForumPost forumPostToUpdate = forumPostRepository.findById(updatedForumPost.getForumPostId()).orElse(null);
                if (forumPostToUpdate != null)
                {
                    forumPostToUpdate.setMessage(updatedForumPost.getMessage());
                    return forumPostToUpdate;
                }
                else
                {
                    throw new ForumPostNotFoundException("Forum Post with ID: " + updatedForumPost.getForumPostId() + " does not exist!");
                }
            }
            else
            {
                throw new InputDataValidationException(FormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new ForumPostNotFoundException("Forum Post ID is not provided for forum post to be updated");
        }
    }

    @Override
    public Boolean deleteForumPost(Long forumPostId) throws ForumPostNotFoundException, DeleteForumPostException
    {
        if (forumPostId != null)
        {
            ForumPost forumPostToDelete = getForumPostByForumPostId(forumPostId);

            List<ForumPost> forumPostReplies = getAllForumPostsByParentForumPostId(forumPostToDelete.getForumPostId());

            if (forumPostReplies != null)
            {
                for (ForumPost forumPostReply : forumPostReplies)
                {
                    deleteForumPost(forumPostReply.getForumPostId());
                }
            }

            return deleteForumPost(forumPostToDelete);
        }
        else
        {
            throw new DeleteForumPostException("ForumPost ID cannot be null");
        }
    }

    @Override
    public Boolean deleteForumPostAndDisassociateFromForumThread(Long forumPostId) throws ForumPostNotFoundException, DeleteForumPostException, UpdateForumThreadException, ForumThreadNotFoundException
    {
        if (forumPostId != null)
        {
            ForumPost forumPostToDelete = getForumPostByForumPostId(forumPostId);
            forumThreadService.removeForumPostToForumThreadByForumPostId(forumPostId);

            List<ForumPost> forumPostReplies = getAllForumPostsByParentForumPostId(forumPostToDelete.getForumPostId());

            if (forumPostReplies != null)
            {
                for (ForumPost forumPostReply : forumPostReplies)
                {
                    deleteForumPostAndDisassociateFromForumThread(forumPostReply.getForumPostId());
                }
            }

            return deleteForumPost(forumPostToDelete);
        }
        else
        {
            throw new DeleteForumPostException("ForumPost ID cannot be null");
        }
    }

    private Boolean deleteForumPost(ForumPost forumPostToDelete)
    {
        if (forumPostToDelete.getParentForumPost() != null)
        {
            forumPostToDelete.setParentForumPost(null);
        }

        forumPostRepository.delete(forumPostToDelete);
        return true;
    }
}
