package com.spring.kodo.service.impl;

import com.spring.kodo.entity.*;
import com.spring.kodo.repository.LessonRepository;
import com.spring.kodo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

import com.spring.kodo.util.MessageFormatterUtil;
import com.spring.kodo.util.exception.*;

@Service
public class LessonServiceImpl implements LessonService
{

    @Autowired // With this annotation, we do not to populate LessonRepository in this class' constructor
    private LessonRepository lessonRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private MultimediaService multimediaService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public LessonServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Lesson createNewLesson(Lesson newLesson, Long courseId) throws InputDataValidationException, CourseNotFoundException
    {
        Set<ConstraintViolation<Lesson>> constraintViolations = validator.validate(newLesson);
        if (constraintViolations.isEmpty())
        {
            Course course = courseService.getCourseByCourseId(courseId);
            course.getLessons().add(newLesson);

            lessonRepository.saveAndFlush(newLesson);
            return newLesson;
        }
        else
        {
            throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
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
    public List<Lesson> getAllLessons()
    {
        return lessonRepository.findAll();
    }

    //only updating attributes, not relationships
    @Override
    public Lesson updateLesson(Long lessonId, Lesson updatedLesson) throws LessonNotFoundException
    {
        Lesson lessonToUpdate = lessonRepository.findById(lessonId).orElse(null);

        if (lessonToUpdate != null)
        {
            lessonToUpdate.setName(updatedLesson.getName());
            lessonToUpdate.setDescription(updatedLesson.getDescription());
            lessonToUpdate.setSequence(updatedLesson.getSequence());
            lessonRepository.saveAndFlush(lessonToUpdate);
            return lessonToUpdate;
        }
        else
        {
            throw new LessonNotFoundException("Lesson with ID: " + lessonId + " does not exist!");
        }
    }

    @Override
    public Boolean deleteLesson(Long lessonId) throws LessonNotFoundException
    {
        Lesson lessonToDelete = lessonRepository.findById(lessonId).orElse(null);

        if (lessonToDelete != null)
        {
            lessonRepository.deleteById(lessonId);
            return true;
        }
        else
        {
            throw new LessonNotFoundException("Lesson with ID: " + lessonId + " does not exist!");
        }
    }

    public Lesson addContentToLesson(Lesson lesson, Content content) throws LessonNotFoundException, UpdateContentException
    {
        lesson = getLessonByLessonId(lesson.getLessonId());

        if (!lesson.getContents().contains(content))
        {
            if (content instanceof Quiz)
            {
                try
                {
                    content = quizService.createNewQuiz((Quiz) content);
                }
                catch (CreateQuizException | InputDataValidationException ex)
                {
                    throw new UpdateContentException(ex.getMessage());
                }
            }
            else if (content instanceof Multimedia)
            {
                try
                {
                    content = multimediaService.createNewMultimedia((Multimedia) content);
                }
                catch (InputDataValidationException | MultimediaExistsException | UnknownPersistenceException ex)
                {
                    throw new UpdateContentException(ex.getMessage());
                }
            }

            lesson.getContents().add(content);
        }
        else
        {
            throw new UpdateContentException("Unable to add content with name: " + content.getName() +
                    " to lesson with ID: " + lesson.getLessonId() + " as content is already linked to this course");
        }

        lessonRepository.saveAndFlush(lesson);
        return lesson;
    }
}
