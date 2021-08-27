package com.spring.kodo.service.impl;

import com.spring.kodo.entity.*;
import com.spring.kodo.entity.Course;
import com.spring.kodo.repository.AccountRepository;
import com.spring.kodo.repository.CourseRepository;
import com.spring.kodo.service.AccountService;
import com.spring.kodo.service.CourseService;
import com.spring.kodo.service.LessonService;
import com.spring.kodo.service.TagService;
import com.spring.kodo.util.MessageFormatterUtil;
import com.spring.kodo.util.exception.*;
import com.spring.kodo.util.exception.CourseNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class CourseServiceImpl implements CourseService
{
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TagService tagService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CourseServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Course createNewCourse(Course newCourse, Long tutorId, List<String> tagTitles) throws InputDataValidationException
    {
        Set<ConstraintViolation<Course>> constraintViolations = validator.validate(newCourse);
        if (constraintViolations.isEmpty())
        {
            // Process Account Tutor
            if (tutorId != null)
            {
                try
                {
                    newCourse = setTutorToCourse(newCourse, tutorId);
                }
                catch (AccountNotFoundException | CourseNotFoundException | UpdateCourseException ex)
                {
                    // Since we are still in a creation step, these generic exceptions will probably not happen / can kinda be ignored
                    // e.g. acc not found, tutor already exists
                }
            }

            // Process Tags
            if (tagTitles != null)
            {
                for (String tagTitle : tagTitles)
                {
                    try
                    {
                        newCourse = addTagToCourse(newCourse, tagTitle);
                    }
                    catch (TagNotFoundException | CourseNotFoundException | UpdateCourseException ex)
                    {
                        // Since we are still in a creation step, these generic exceptions will probably not happen / can kinda be ignored
                        // e.g. acc not found, duplicate tag exception
                        continue;
                    }
                }
            }

            courseRepository.saveAndFlush(newCourse);
            return newCourse;
        }
        else
        {
            throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public Course getCourseByCourseId(Long courseId) throws CourseNotFoundException
    {
        Course course = courseRepository.findById(courseId).orElse(null);

        if (course != null)
        {
            return course;
        }
        else
        {
            throw new CourseNotFoundException("Course with ID: " + courseId + " does not exist!");
        }
    }

    @Override
    public Course getCourseByName(String name) throws CourseNotFoundException
    {
        Course course = courseRepository.findByName(name).orElse(null);

        if (course != null)
        {
            return course;
        }
        else
        {
            throw new CourseNotFoundException("Course with Name: " + name + " does not exist!");
        }
    }

    @Override
    public List<Course> getAllCourses()
    {
        return courseRepository.findAll();
    }

    @Override
    public Course addTagToCourse(Course course, String tagTitle) throws InputDataValidationException, CourseNotFoundException, TagNotFoundException, UpdateCourseException
    {
        Tag tag = tagService.getTagByTitleOrCreateNew(tagTitle);

        if (!course.getCourseTags().contains(tag))
        {
            course.getCourseTags().add(tag);
        }
        else
        {
            throw new UpdateCourseException("Unable to add tag with title: " + tag.getTitle() +
                    " to course with ID: " + course.getCourseId() + " as tag is already linked to this course");
        }

        return course;
    }

    @Override
    public Course addLessonToCourse(Course course, Lesson lesson) throws CourseNotFoundException, InputDataValidationException, UpdateCourseException
    {
        course = getCourseByCourseId(course.getCourseId());

        if (!course.getLessons().contains(lesson))
        {
            lesson = lessonService.createNewLesson(lesson);
            course.getLessons().add(lesson);
        }
        else
        {
            throw new UpdateCourseException("Unable to add lesson with name: " + lesson.getName() +
                    " to course with ID: " + course.getCourseId() + " as tag is already linked to this course");
        }

        return course;
    }

    private Course setTutorToCourse(Course course, Long tutorId) throws CourseNotFoundException, AccountNotFoundException, UpdateCourseException
    {
        Account tutor = accountService.getAccountByAccountId(tutorId);

        if (course.getTutor() == null)
        {
            course.setTutor(tutor);
            tutor.getCourses().add(course);
        }
        else
        {
            throw new UpdateCourseException("Unable to add tutor with name: " + tutor.getName() +
                    " to course with Name: " + course.getName() + " as there is already a tutor linked to this course");
        }

        return course;
    }
}
