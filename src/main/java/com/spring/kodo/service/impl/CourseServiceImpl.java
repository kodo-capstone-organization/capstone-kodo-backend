package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.ForumCategory;
import com.spring.kodo.entity.Lesson;
import com.spring.kodo.entity.Tag;
import com.spring.kodo.repository.CourseRepository;
import com.spring.kodo.service.inter.*;
import com.spring.kodo.util.MessageFormatterUtil;
import com.spring.kodo.util.exception.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    private ForumCategoryService forumCategoryService;

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
    public Course createNewCourse(Course newCourse, Long tutorId, List<String> tagTitles)
            throws CreateNewCourseException,
            UpdateCourseException,
            TagNotFoundException,
            AccountNotFoundException,
            CourseNotFoundException,
            TagNameExistsException,
            UnknownPersistenceException,
            InputDataValidationException
    {
        try
        {
            Set<ConstraintViolation<Course>> constraintViolations = validator.validate(newCourse);
            if (constraintViolations.isEmpty())
            {
                // Process Account Tutor
//                if (tutorId != null)
//                {
//                    newCourse = setTutorToCourse(newCourse, tutorId);

                // Process Tags
                if (tagTitles != null)
                {
                    for (String tagTitle : tagTitles)
                    {
                        newCourse = addTagToCourse(newCourse, tagTitle);
                    }

                    courseRepository.saveAndFlush(newCourse);
                    return newCourse;
                }
                else
                {
                    throw new CreateNewCourseException("TagTitles cannot be null");
                }
//                }
//                else
//                {
//                    throw new CreateNewCourseException("TutorId cannot be null");
//                }
            }
            else
            {
                throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        catch (DataAccessException ex)
        {
            throw new UnknownPersistenceException(ex.getMessage());
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
    public List<Course> getAllCoursesByTagTitle(String tagTitle) throws TagNotFoundException
    {
        tagService.getTagByTitle(tagTitle);
        return courseRepository.findCoursesByTagTitle(tagTitle);
    }

    @Override
    public List<Course> getAllCoursesByKeyword(String keyword) throws CourseWithKeywordNotFoundException
    {
        List<Course> courses = courseRepository.findCoursesByKeyword(keyword);

        if (courses.size() > 0)
        {
            return courses;
        }
        else
        {
            throw new CourseWithKeywordNotFoundException("Courses with the keyword " + keyword + " cannot be found");
        }
    }

    @Override
    public List<Course> getAllCoursesByTutorId(Long tutorId) throws AccountNotFoundException
    {
        accountService.getAccountByAccountId(tutorId);
        return courseRepository.findCoursesByTutorId(tutorId);
    }

    @Override
    public Course addTagToCourse(Course course, String tagTitle) throws CourseNotFoundException, TagNotFoundException, UpdateCourseException, TagNameExistsException, UnknownPersistenceException, InputDataValidationException
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
    public Course addTagToCourse(Course course, Tag tag) throws UpdateCourseException, CourseNotFoundException, TagNotFoundException
    {
        if (course != null)
        {
            if (course.getCourseId() != null)
            {
                course = getCourseByCourseId(course.getCourseId());
                if (tag != null)
                {
                    if (tag.getTagId() != null)
                    {
                        tag = tagService.getTagByTagId(tag.getTagId());

                        if (!course.getCourseTags().contains(tag))
                        {
                            course.getCourseTags().add(tag);

                            courseRepository.save(course);
                            return course;
                        }
                        else
                        {
                            throw new UpdateCourseException("Course with ID " + course.getCourseId() + " already contains Tag with ID " + tag.getTagId());
                        }
                    }
                    else
                    {
                        throw new UpdateCourseException("Tag ID cannot be null");
                    }
                }
                else
                {
                    throw new UpdateCourseException("Tag cannot be null");
                }
            }
            else
            {
                throw new UpdateCourseException("Course ID cannot be null");
            }
        }
        else
        {
            throw new UpdateCourseException("Course cannot be null");
        }
    }

    @Override
    public Course addLessonToCourse(Course course, Lesson lesson) throws UpdateCourseException, CourseNotFoundException, LessonNotFoundException
    {
        if (course != null)
        {
            if (course.getCourseId() != null)
            {
                course = getCourseByCourseId(course.getCourseId());
                if (lesson != null)
                {
                    if (lesson.getLessonId() != null)
                    {
                        lesson = lessonService.getLessonByLessonId(lesson.getLessonId());

                        if (!course.getLessons().contains(lesson))
                        {
                            course.getLessons().add(lesson);

                            courseRepository.save(course);
                            return course;
                        }
                        else
                        {
                            throw new UpdateCourseException("Course with ID " + course.getCourseId() + " already contains Lesson with ID " + lesson.getLessonId());
                        }
                    }
                    else
                    {
                        throw new UpdateCourseException("Lesson ID cannot be null");
                    }
                }
                else
                {
                    throw new UpdateCourseException("Lesson cannot be null");
                }
            }
            else
            {
                throw new UpdateCourseException("Course ID cannot be null");
            }
        }
        else
        {
            throw new UpdateCourseException("Course cannot be null");
        }
    }

    @Override
    public Course addForumCategoryToCourse(Course course, ForumCategory forumCategory) throws UpdateCourseException, CourseNotFoundException, ForumCategoryNotFoundException
    {
        if (course != null)
        {
            if (course.getCourseId() != null)
            {
                course = getCourseByCourseId(course.getCourseId());
                if (forumCategory != null)
                {
                    if (forumCategory.getForumCategoryId() != null)
                    {
                        forumCategory = forumCategoryService.getForumCategoryByForumCategoryId(forumCategory.getForumCategoryId());

                        if (!course.getForumCategories().contains(forumCategory))
                        {
                            course.getForumCategories().add(forumCategory);

                            courseRepository.save(course);
                            return course;
                        }
                        else
                        {
                            throw new UpdateCourseException("Course with ID " + course.getCourseId() + " already contains ForumCategory with ID " + forumCategory.getForumCategoryId());
                        }
                    }
                    else
                    {
                        throw new UpdateCourseException("ForumCategory ID cannot be null");
                    }
                }
                else
                {
                    throw new UpdateCourseException("ForumCategory cannot be null");
                }
            }
            else
            {
                throw new UpdateCourseException("Course ID cannot be null");
            }
        }
        else
        {
            throw new UpdateCourseException("Course cannot be null");
        }
    }

//    private Course setTutorToCourse(Course course, Long tutorId) throws AccountNotFoundException, UpdateCourseException
//    {
//        Account tutor = accountService.getAccountByAccountId(tutorId);
//
//        if (course.getTutor() == null)
//        {
//            course.setTutor(tutor);
//            tutor.getCourses().add(course);
//        }
//        else
//        {
//            throw new UpdateCourseException("Unable to add tutor with name: " + tutor.getName() +
//                    " to course with Name: " + course.getName() + " as there is already a tutor linked to this course");
//        }
//
//        return course;
//    }
}
