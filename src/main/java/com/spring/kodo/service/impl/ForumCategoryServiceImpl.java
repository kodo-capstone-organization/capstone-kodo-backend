package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.ForumCategory;
import com.spring.kodo.entity.ForumThread;
import com.spring.kodo.repository.ForumCategoryRepository;
import com.spring.kodo.service.inter.CourseService;
import com.spring.kodo.service.inter.ForumCategoryService;
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
public class ForumCategoryServiceImpl implements ForumCategoryService
{
    @Autowired // With this annotation, we do not to populate ForumCategoryRepository in this class' constructor
    private ForumCategoryRepository forumCategoryRepository;

    @Autowired
    private CourseService courseService;

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
    public ForumCategory createNewForumCategory(ForumCategory newForumCategory, Long courseId) throws InputDataValidationException, UnknownPersistenceException, CourseNotFoundException
    {
        try
        {
            Set<ConstraintViolation<ForumCategory>> constraintViolations = validator.validate(newForumCategory);
            if (constraintViolations.isEmpty())
            {
                Course course = courseService.getCourseByCourseId(courseId);
                newForumCategory.setCourse(course);
                forumCategoryRepository.saveAndFlush(newForumCategory);
                return newForumCategory;
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

    @Override
    public List<ForumCategory> getAllForumCategoriesByCourseId(Long courseId) throws ForumCategoryNotFoundException
    {
        List<ForumCategory> forumCategoryList = forumCategoryRepository.findAllByCourseId(courseId);

        if (forumCategoryList != null)
        {
            return forumCategoryList;
        }
        else
        {
            throw new ForumCategoryNotFoundException("Forum Categories with course ID: " + courseId + " does not exist!");
        }
    }

    @Override
    public ForumCategory getForumCategoryByForumThreadId(Long forumThreadId) throws ForumCategoryNotFoundException
    {
        ForumCategory forumCategory = forumCategoryRepository.findByForumThreadId(forumThreadId).orElse(null);

        if (forumCategory != null)
        {
            return forumCategory;
        }
        else
        {
            throw new ForumCategoryNotFoundException("Forum Category with ForumThread ID: " + forumThreadId + " does not exist!");
        }
    }

    @Override
    public List<ForumCategory> getAllForumCategories()
    {
        return forumCategoryRepository.findAll();
    }

    //only updating attributes, not relationships
    @Override
    public ForumCategory updateForumCategory(ForumCategory updatedForumCategory) throws ForumCategoryNotFoundException, InputDataValidationException
    {
        if (updatedForumCategory != null && updatedForumCategory.getForumCategoryId() != null)
        {
            Set<ConstraintViolation<ForumCategory>> constraintViolations = validator.validate(updatedForumCategory);
            if (constraintViolations.isEmpty())
            {
                ForumCategory forumCategoryToUpdate = forumCategoryRepository.findById(updatedForumCategory.getForumCategoryId()).orElse(null);
                if (forumCategoryToUpdate != null)
                {
                    forumCategoryToUpdate.setName(updatedForumCategory.getName());
                    forumCategoryToUpdate.setDescription(updatedForumCategory.getDescription());
                    forumCategoryRepository.saveAndFlush(forumCategoryToUpdate);
                    return forumCategoryToUpdate;
                }
                else
                {
                    throw new ForumCategoryNotFoundException("Forum Category with ID: " + updatedForumCategory.getForumCategoryId() + " does not exist!");
                }
            }
            else
            {
                throw new InputDataValidationException(FormatterHelper.prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new ForumCategoryNotFoundException("Forum Category ID is not provided for forum category to be updated");
        }
    }

    @Override
    public Boolean deleteForumCategory(Long forumCategoryId) throws DeleteForumCategoryException, ForumCategoryNotFoundException, DeleteForumThreadException, DeleteForumPostException, ForumThreadNotFoundException, ForumPostNotFoundException
    {
        if (forumCategoryId != null)
        {
            ForumCategory forumCategoryToDelete = getForumCategoryByForumCategoryId(forumCategoryId);
            List<ForumThread> forumThreadsToDelete = new ArrayList<>(forumCategoryToDelete.getForumThreads());

            forumCategoryToDelete.getForumThreads().clear();
            for (ForumThread forumThreadToDelete : forumThreadsToDelete)
            {
                forumThreadService.deleteForumThread(forumThreadToDelete.getForumThreadId());
            }

            forumCategoryRepository.delete(forumCategoryToDelete);
            return true;
        }
        else
        {
            throw new DeleteForumCategoryException("Forum Category ID cannot be null");
        }
    }

    @Override
    public ForumCategory addForumThreadToForumCategory(ForumCategory forumCategory, ForumThread forumThread) throws UpdateForumCategoryException, ForumCategoryNotFoundException, ForumThreadNotFoundException
    {
        if (forumCategory != null)
        {
            if (forumCategory.getForumCategoryId() != null)
            {
                forumCategory = getForumCategoryByForumCategoryId(forumCategory.getForumCategoryId());
                if (forumThread != null)
                {
                    if (forumThread.getForumThreadId() != null)
                    {
                        forumThread = forumThreadService.getForumThreadByForumThreadId(forumThread.getForumThreadId());

                        if (!forumCategory.getForumThreads().contains(forumThread))
                        {
                            forumCategory.getForumThreads().add(forumThread);

                            forumCategoryRepository.save(forumCategory);
                            return forumCategory;
                        }
                        else
                        {
                            throw new UpdateForumCategoryException("ForumCategory with ID " + forumCategory.getForumCategoryId() + " already contains ForumThread with ID " + forumThread.getForumThreadId());
                        }
                    }
                    else
                    {
                        throw new UpdateForumCategoryException("ForumThread ID cannot be null");
                    }
                }
                else
                {
                    throw new UpdateForumCategoryException("ForumThread cannot be null");
                }
            }
            else
            {
                throw new UpdateForumCategoryException("ForumCategory ID cannot be null");
            }
        }
        else
        {
            throw new UpdateForumCategoryException("ForumCategory cannot be null");
        }
    }

    @Override
    public ForumCategory removeForumThreadToForumCategoryByForumThreadId(Long forumThreadId) throws UpdateForumCategoryException, ForumCategoryNotFoundException
    {
        if (forumThreadId != null)
        {
            ForumCategory forumCategoryToUpdate = getForumCategoryByForumThreadId(forumThreadId);
            List<ForumThread> forumThreads = forumCategoryToUpdate.getForumThreads();

            for (int i = 0; i < forumThreads.size(); i++)
            {
                if (forumThreads.get(i).getForumThreadId().equals(forumThreadId))
                {
                    forumCategoryToUpdate.getForumThreads().remove(i);
                    break;
                }
            }

            forumCategoryRepository.saveAndFlush(forumCategoryToUpdate);
            return forumCategoryToUpdate;
        }
        else
        {
            throw new UpdateForumCategoryException("ForumPost ID cannot be null");
        }
    }
}