package com.spring.kodo.service.impl;

import com.spring.kodo.entity.*;
import com.spring.kodo.repository.LessonRepository;
import com.spring.kodo.service.inter.*;
import com.spring.kodo.util.FormatterUtil;
import com.spring.kodo.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LessonServiceImpl implements LessonService
{

    @Autowired // With this annotation, we do not to populate LessonRepository in this class' constructor
    private LessonRepository lessonRepository;

    @Autowired
    private ContentService contentService;

    @Autowired
    private EnrolledLessonService enrolledLessonService;

    @Autowired
    private CourseService courseService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public LessonServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Lesson createNewLesson(Lesson newLesson) throws InputDataValidationException, UnknownPersistenceException
    {
        try
        {
            Set<ConstraintViolation<Lesson>> constraintViolations = validator.validate(newLesson);
            if (constraintViolations.isEmpty())
            {
                lessonRepository.saveAndFlush(newLesson);
                return newLesson;
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
    public Lesson getLessonByLessonId(Long lessonId) throws LessonNotFoundException
    {
        Lesson lesson = lessonRepository.findById(lessonId).orElse(null);

        if (lesson != null)
        {
            return lesson;
        }
        else
        {
            throw new LessonNotFoundException("Lesson with ID: " + lessonId + " does not exist!");
        }
    }

    @Override
    public Lesson getLessonByName(String name) throws LessonNotFoundException
    {
        Lesson lesson = lessonRepository.findByName(name).orElse(null);

        if (lesson != null)
        {
            return lesson;
        }
        else
        {
            throw new LessonNotFoundException("Lesson with Name: " + name + " does not exist!");
        }
    }

    @Override
    public Lesson getLessonByContentId(Long contentId) throws LessonNotFoundException
    {
        Lesson lesson = lessonRepository.findByContentId(contentId).orElse(null);

        if (lesson != null)
        {
            return lesson;
        }
        else
        {
            throw new LessonNotFoundException("Lesson with Content ID: " + contentId + " does not exist!");
        }
    }

    @Override
    public List<Lesson> getAllLessons()
    {
        return lessonRepository.findAll();
    }

    @Override
    public Lesson updateLesson(Lesson lesson, List<Long> contentIds) throws LessonNotFoundException, UpdateContentException, UnknownPersistenceException, ContentNotFoundException, InputDataValidationException {

        if (lesson != null && lesson.getLessonId() != null)
        {
            Lesson lessonToUpdate = null;
            Set<ConstraintViolation<Lesson>> constraintViolations = validator.validate(lesson);

            if (constraintViolations.isEmpty())
            {
                lessonToUpdate = getLessonByLessonId(lesson.getLessonId());

                lessonToUpdate.getContents().clear();

                if (contentIds != null)
                {
                    lessonToUpdate.getContents().clear();
                    for (Long contentId: contentIds)
                    {
                        Content content = contentService.getContentByContentId(contentId);
                        addContentToLesson(lessonToUpdate, content);
                    }
                }

                // Update non-relational fields
                lessonToUpdate.setName(lesson.getName());
                lessonToUpdate.setDescription(lesson.getDescription());
                lessonToUpdate.setSequence(lesson.getSequence());
                return lessonRepository.saveAndFlush(lessonToUpdate);
            }
            else
            {
                throw new InputDataValidationException(FormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new LessonNotFoundException("Lesson ID not provided for Lesson to be updated");
        }
    }

    @Override
    public Boolean deleteLesson(Long lessonId) throws LessonNotFoundException, CourseNotFoundException, TagNotFoundException, EnrolledCourseNotFoundException, UpdateCourseException, TagNameExistsException, InputDataValidationException, UnknownPersistenceException {
        Lesson lessonToDelete = getLessonByLessonId(lessonId);

        if (lessonToDelete != null)
        {
            // Check for enrolled lessons and un-set parent lesson
            List<EnrolledLesson> enrolledLessons = enrolledLessonService.getAllEnrolledLessonsByParentLessonId(lessonId);
            for (EnrolledLesson enrolledLesson: enrolledLessons)
            {
                enrolledLesson.setParentLesson(null);
                // TODO: enrolledLessonService.updateEnrolledLesson()
            }

            // Check for course with the lesson
            Course courseWithLesson = courseService.getCourseByLessonId(lessonId);
            List<Lesson> newLessons = courseWithLesson.getLessons();
            newLessons.remove(lessonToDelete);
            courseService.updateCourse(courseWithLesson, null, newLessons.stream().map(Lesson::getLessonId).collect(Collectors.toList()), null);

            lessonRepository.deleteById(lessonId);
            return true;
        }
        else
        {
            throw new LessonNotFoundException("Lesson with ID: " + lessonId + " does not exist!");
        }
    }

    public Lesson addContentToLesson(Lesson lesson, Content content) throws LessonNotFoundException, UpdateContentException, ContentNotFoundException
    {
        lesson = getLessonByLessonId(lesson.getLessonId());
        content = contentService.getContentByContentId(content.getContentId());

        if (!lesson.getContents().contains(content))
        {
            lesson.getContents().add(content);
        }
        else
        {
            throw new UpdateContentException("Unable to add content with name: " + content.getName() +
                    " to lesson with ID: " + lesson.getLessonId() + " as content is already linked to this course");
        }

        return lessonRepository.saveAndFlush(lesson);
    }
}
