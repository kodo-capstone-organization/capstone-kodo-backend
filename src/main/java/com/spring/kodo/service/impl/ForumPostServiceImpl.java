package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.ForumPost;
import com.spring.kodo.entity.ForumThread;
import com.spring.kodo.repository.ForumPostRepository;
import com.spring.kodo.service.inter.AccountService;
import com.spring.kodo.service.inter.ForumPostService;
import com.spring.kodo.service.inter.ForumThreadService;
import com.spring.kodo.util.helper.FormatterHelper;
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
                throw new InputDataValidationException(FormatterHelper.prepareInputDataValidationErrorsMessage(constraintViolations));
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
                parentForumPost.getReplies().add(newForumPostReply);
                newForumPostReply.setParentForumPost(parentForumPost);

                forumPostRepository.saveAndFlush(newForumPostReply);
                forumPostRepository.saveAndFlush(parentForumPost);
                return newForumPostReply;
            }
            else
            {
                throw new InputDataValidationException(FormatterHelper.prepareInputDataValidationErrorsMessage(constraintViolations));
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
    public List<ForumPost> getAllByNullParentPostAndForumThreadId(Long forumThreadId)
    {
        return forumPostRepository.findAllByNullParentPostAndForumThreadId(forumThreadId);
    }

    @Override
    public List<ForumPost> getAllForumPostsByForumThreadId(Long forumThreadId) throws ForumThreadNotFoundException
    {
        ForumThread forumThread = forumThreadService.getForumThreadByForumThreadId(forumThreadId);
        return forumThread.getForumPosts();
    }

    @Override
    public List<ForumPost> getReportedForumPostsByThreadId(Long forumThreadId)
    {
        return forumPostRepository.findAllReportedForumPostsByThreadId(forumThreadId);
    }

    @Override
    public Long toggleReport(Long forumPostId, Long requestingAccountId) throws AccountNotFoundException, ForumPostNotFoundException, AccountPermissionDeniedException {
        Account requestingAccount = accountService.getAccountByAccountId(requestingAccountId);
        ForumPost forumPostToUpdate = getForumPostByForumPostId(forumPostId);

        if (requestingAccount.getIsAdmin())
        {
            // Toggle
            forumPostToUpdate.setReported(false);
            forumPostToUpdate.setReasonForReport("");

            return forumPostRepository.saveAndFlush(forumPostToUpdate).getForumPostId();
        }
        else
        {
            throw new AccountPermissionDeniedException("You do not have the rights to toggle enrollment status of this course");
        }
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
                    if (updatedForumPost.getReasonForReport() != null)
                    {
                        forumPostToUpdate.setReported(true);
                        forumPostToUpdate.setReasonForReport(updatedForumPost.getReasonForReport());
                    }
                    else
                    {
                        forumPostToUpdate.setReported(false);
                        forumPostToUpdate.setReasonForReport(null);
                    }

                    forumPostRepository.saveAndFlush(forumPostToUpdate);

                    return forumPostToUpdate;
                }
                else
                {
                    throw new ForumPostNotFoundException("Forum Post with ID: " + updatedForumPost.getForumPostId() + " does not exist!");
                }
            }
            else
            {
                throw new InputDataValidationException(FormatterHelper.prepareInputDataValidationErrorsMessage(constraintViolations));
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

            // Remove reply related
            if (forumPostToDelete.getReplies().size() > 0)
            {
                List<ForumPost> forumPostReplies = new ArrayList<>(forumPostToDelete.getReplies());
                forumPostToDelete.getReplies().clear();

                for (ForumPost forumPostReply : forumPostReplies)
                {
                    deleteForumPost(forumPostReply.getForumPostId());
                }
            }

            forumPostRepository.delete(forumPostToDelete);
            return true;
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

            // Remove parent related
            if (forumPostToDelete.getParentForumPost() != null)
            {
                List<ForumPost> parentForumPostReplies = forumPostToDelete.getParentForumPost().getReplies();
                ForumPost parentForumPostReply;

                for (int i = 0; i < parentForumPostReplies.size(); i++)
                {
                    parentForumPostReply = parentForumPostReplies.get(i);
                    if (parentForumPostReply.getForumPostId().equals(forumPostToDelete.getForumPostId()))
                    {
                        parentForumPostReplies.remove(i);
                        break;
                    }
                }
                forumPostToDelete.setParentForumPost(null);
            }

            // Remove reply related
            if (forumPostToDelete.getReplies().size() > 0)
            {
                List<ForumPost> forumPostReplies = new ArrayList<>(forumPostToDelete.getReplies());
                forumPostToDelete.getReplies().clear();

                for (ForumPost forumPostReply : forumPostReplies)
                {
                    deleteForumPostAndDisassociateFromForumThread(forumPostReply.getForumPostId());
                }
            }

            forumPostRepository.delete(forumPostToDelete);
            return true;
        }
        else
        {
            throw new DeleteForumPostException("ForumPost ID cannot be null");
        }
    }


}
