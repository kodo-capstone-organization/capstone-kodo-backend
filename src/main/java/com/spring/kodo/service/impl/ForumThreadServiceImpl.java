package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.ForumCategory;
import com.spring.kodo.entity.ForumPost;
import com.spring.kodo.entity.ForumThread;
import com.spring.kodo.repository.ForumThreadRepository;
import com.spring.kodo.service.inter.AccountService;
import com.spring.kodo.service.inter.ForumCategoryService;
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
public class ForumThreadServiceImpl implements ForumThreadService
{
    @Autowired // With this annotation, we do not to populate ForumThreadRepository in this class' constructor
    private ForumThreadRepository forumThreadRepository;

    @Autowired
    private AccountService accountService;

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
    public ForumThread createNewForumThread(ForumThread newForumThread, Long accountId) throws InputDataValidationException, UnknownPersistenceException, AccountNotFoundException
    {
        try
        {
            Set<ConstraintViolation<ForumThread>> constraintViolations = validator.validate(newForumThread);
            if (constraintViolations.isEmpty())
            {
                Account account = accountService.getAccountByAccountId(accountId);
                newForumThread.setAccount(account);
                forumThreadRepository.saveAndFlush(newForumThread);
                return newForumThread;
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
    public ForumThread getForumThreadByForumPostId(Long forumPostId) throws ForumThreadNotFoundException
    {
        ForumThread forumThread = forumThreadRepository.findByForumPostId(forumPostId).orElse(null);

        if (forumThread != null)
        {
            return forumThread;
        }
        else
        {
            throw new ForumThreadNotFoundException("Forum Thread with ForumPost ID: " + forumPostId + " does not exist!");
        }
    }

    @Override
    public List<ForumThread> getAllForumThreads()
    {
        return forumThreadRepository.findAll();
    }

    @Override
    public List<ForumThread> getAllForumThreadsByForumCategoryId(Long forumCategoryId) throws ForumCategoryNotFoundException
    {
        return forumThreadRepository.findAllByForumCategoryId(forumCategoryId);
    }

    //only updating attributes, not relationships
    @Override
    public ForumThread updateForumThread(ForumThread updatedForumThread) throws
            ForumThreadNotFoundException, InputDataValidationException
    {
        if (updatedForumThread != null && updatedForumThread.getForumThreadId() != null)
        {
            Set<ConstraintViolation<ForumThread>> constraintViolations = validator.validate(updatedForumThread);
            if (constraintViolations.isEmpty())
            {
                ForumThread forumThreadToUpdate = forumThreadRepository.findById(updatedForumThread.getForumThreadId()).orElse(null);
                if (forumThreadToUpdate != null)
                {
                    forumThreadToUpdate.setName(updatedForumThread.getName());
                    forumThreadToUpdate.setDescription(updatedForumThread.getDescription());
                    return forumThreadToUpdate;
                }
                else
                {
                    throw new ForumThreadNotFoundException("Forum Thread with ID: " + updatedForumThread.getForumThreadId() + " does not exist!");
                }
            }
            else
            {
                throw new InputDataValidationException(FormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new ForumThreadNotFoundException("Forum Thread ID is not provided for forum thread to be updated");
        }
    }

    @Override
    public Boolean deleteForumThread(Long forumThreadId) throws DeleteForumThreadException, ForumThreadNotFoundException, DeleteForumPostException, ForumPostNotFoundException
    {
        if (forumThreadId != null)
        {
            ForumThread forumThreadToDelete = getForumThreadByForumThreadId(forumThreadId);

            return deleteForumThread(forumThreadToDelete);
        }
        else
        {
            throw new DeleteForumThreadException("Forum Thread ID cannot be null");
        }
    }

    @Override
    public Boolean deleteForumThreadAndDisassociateFromForumCategory(Long forumThreadId) throws DeleteForumThreadException, ForumThreadNotFoundException, DeleteForumPostException, ForumPostNotFoundException, ForumCategoryNotFoundException, UpdateForumCategoryException
    {
        if (forumThreadId != null)
        {
            ForumThread forumThreadToDelete = getForumThreadByForumThreadId(forumThreadId);
            forumCategoryService.removeForumThreadToForumCategoryByForumThreadId(forumThreadId);

            return deleteForumThread(forumThreadToDelete);
        }
        else
        {
            throw new DeleteForumThreadException("Forum Thread ID cannot be null");
        }
    }

    private Boolean deleteForumThread(ForumThread forumThreadToDelete) throws DeleteForumPostException, ForumPostNotFoundException
    {
        List<ForumPost> forumPostsToDelete = new ArrayList<>(forumThreadToDelete.getForumPosts());
        forumPostsToDelete = forumPostsToDelete.stream().filter(forumPostToDelete -> forumPostToDelete.getParentForumPost() == null).toList();

        forumThreadToDelete.getForumPosts().clear();
        for (ForumPost forumPostToDelete : forumPostsToDelete)
        {
            forumPostService.deleteForumPost(forumPostToDelete.getForumPostId());
        }

        forumThreadRepository.delete(forumThreadToDelete);
        return true;
    }

    @Override
    public ForumThread addForumPostToForumThread(ForumThread forumThread, ForumPost forumPost) throws UpdateForumThreadException, ForumThreadNotFoundException, ForumPostNotFoundException
    {
        if (forumThread != null)
        {
            if (forumThread.getForumThreadId() != null)
            {
                forumThread = getForumThreadByForumThreadId(forumThread.getForumThreadId());
                if (forumPost != null)
                {
                    if (forumPost.getForumPostId() != null)
                    {
                        forumPost = forumPostService.getForumPostByForumPostId(forumPost.getForumPostId());

                        if (!forumThread.getForumPosts().contains(forumPost))
                        {
                            forumThread.getForumPosts().add(forumPost);
                            forumThreadRepository.save(forumThread);
                            return forumThread;
                        }
                        else
                        {
                            throw new UpdateForumThreadException("ForumThread with ID " + forumThread.getForumThreadId() + " already contains ForumPost with ID " + forumPost.getForumPostId());
                        }
                    }
                    else
                    {
                        throw new UpdateForumThreadException("ForumPost ID cannot be null");
                    }
                }
                else
                {
                    throw new UpdateForumThreadException("ForumPost cannot be null");
                }
            }
            else
            {
                throw new UpdateForumThreadException("ForumThread ID cannot be null");
            }
        }
        else
        {
            throw new UpdateForumThreadException("ForumThread cannot be null");
        }
    }

    @Override
    public ForumThread removeForumPostToForumThreadByForumPostId(Long forumPostId) throws UpdateForumThreadException, ForumThreadNotFoundException
    {
        if (forumPostId != null)
        {
            ForumThread forumThreadToUpdate = getForumThreadByForumPostId(forumPostId);
            List<ForumPost> forumPosts = forumThreadToUpdate.getForumPosts();

            for (int i = 0; i < forumPosts.size(); i++)
            {
                if (forumPosts.get(i).getForumPostId().equals(forumPostId))
                {
                    forumThreadToUpdate.getForumPosts().remove(i);
                    break;
                }
            }

            forumThreadRepository.saveAndFlush(forumThreadToUpdate);
            return forumThreadToUpdate;
        }
        else
        {
            throw new UpdateForumThreadException("ForumPost ID cannot be null");
        }
    }
}
