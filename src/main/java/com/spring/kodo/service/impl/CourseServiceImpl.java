package com.spring.kodo.service.impl;

import com.spring.kodo.entity.*;
import com.spring.kodo.repository.CourseRepository;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    private EnrolledCourseService enrolledCourseService;

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
    public Course getCourseByLessonId(Long lessonId) throws CourseNotFoundException
    {
        Course course = courseRepository.findByLessonId(lessonId).orElse(null);

        if (course != null)
        {
            return course;
        }
        else
        {
            throw new CourseNotFoundException("Course with Lesson ID: " + lessonId + " does not exist!");
        }
    }

    @Override
    public List<Course> getAllCoursesWithActiveEnrollment()
    {
        return courseRepository.findAllWithActiveEnrollment();
    }

    @Override
    public List<Course> getAllCourses()
    {
        return courseRepository.findAll();
    }

    @Override
    public List<Course> getAllCoursesByTagTitle(String tagTitle)
    {
        List<Course> courses = courseRepository.findAllCoursesByTagTitle(tagTitle);

        return courses;
    }

    @Override
    public List<Course> getAllCoursesByTagId(Long tagId)
    {
        List<Course> courses = courseRepository.findAllCoursesByTagId(tagId);

        return courses;
    }

    @Override
    public List<Course> getAllCoursesByKeyword(String keyword) throws CourseWithKeywordNotFoundException
    {
        List<Course> courses = courseRepository.findAllCoursesByKeyword(keyword);

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
        return courseRepository.findAllCoursesByTutorId(tutorId);
    }

    @Override
    public List<Course> getAllCoursesInTheLast14Days()
    {
        return courseRepository.findAllCoursesCreatedInTheLast14Days();
    }

    @Override
    public List<Course> getAllCoursesThatArePopular()
    {
        return courseRepository.findAllCoursesThatArePopular();
    }

    @Override
    public Course updateCourse(
            Course course,
            List<Long> enrolledCourseIds,
            List<Long> lessonIds,
            List<String> courseTagTitles
    ) throws InputDataValidationException, CourseNotFoundException, UpdateCourseException, TagNameExistsException, UnknownPersistenceException, TagNotFoundException, LessonNotFoundException, EnrolledCourseNotFoundException
    {

        if (course != null && course.getCourseId() != null)
        {
            Course courseToUpdate = null;
            Set<ConstraintViolation<Course>> constraintViolations = validator.validate(course);

            if (constraintViolations.isEmpty())
            {
                courseToUpdate = getCourseByCourseId(course.getCourseId());

                // Update tags (courseTags) - Unidirectional
                if (courseTagTitles != null)
                {
                    courseToUpdate.getCourseTags().clear();
                    for (String courseTagTitle : courseTagTitles)
                    {
                        Tag tag = tagService.getTagByTitleOrCreateNew(courseTagTitle);
                        addTagToCourse(courseToUpdate, tag);
                    }
                }

                // Update lessons - Unidirectional
                if (lessonIds != null)
                {

                    // Find lessons that were removed to delete
                    List<Long> currentLessonIds = courseToUpdate.getLessons().stream().map(Lesson::getLessonId).collect(Collectors.toList());
                    for (Long curLessonId : currentLessonIds)
                    {
                        if (!lessonIds.contains(curLessonId))
                        {
                            lessonService.deleteLesson(curLessonId);
                        }
                    }

                    // Make association
                    courseToUpdate.getLessons().clear();
                    for (Long lessonId : lessonIds)
                    {
                        Lesson lesson = lessonService.getLessonByLessonId(lessonId);
                        addLessonToCourse(courseToUpdate, lesson);
                    }
                }

                // Update enrollment - Bidirectional
                if (enrolledCourseIds != null)
                {
                    for (EnrolledCourse enrolledCourse : courseToUpdate.getEnrollment())
                    {
                        enrolledCourse.setParentCourse(null);
                    }
                    courseToUpdate.getEnrollment().clear();

                    for (Long enrolledCourseId : enrolledCourseIds)
                    {
                        EnrolledCourse enrolledCourse = enrolledCourseService.getEnrolledCourseByEnrolledCourseId(enrolledCourseId);
                        addEnrolledCourseToCourse(courseToUpdate, enrolledCourse);
                    }
                }

                // Update other non-relational fields
                courseToUpdate.setName(course.getName());
                courseToUpdate.setDescription(course.getDescription());
                courseToUpdate.setPrice(course.getPrice());
                courseToUpdate.setBannerUrl(course.getBannerUrl());

                return courseRepository.saveAndFlush(courseToUpdate);

            }
            else
            {
                throw new InputDataValidationException(FormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
            }

        }
        else
        {
            throw new CourseNotFoundException("Course ID not provided for course to be updated");
        }
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
                            return courseRepository.save(course);
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
                            return courseRepository.save(course);
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
    public Course addEnrolledCourseToCourse(Course course, EnrolledCourse enrolledCourse) throws CourseNotFoundException, UpdateCourseException, EnrolledCourseNotFoundException
    {

        if (course != null)
        {
            if (course.getCourseId() != null)
            {
                course = getCourseByCourseId(course.getCourseId());
                if (enrolledCourse != null)
                {
                    if (enrolledCourse.getEnrolledCourseId() != null)
                    {
                        enrolledCourse = enrolledCourseService.getEnrolledCourseByEnrolledCourseId(enrolledCourse.getEnrolledCourseId());

                        if (!course.getEnrollment().contains(enrolledCourse))
                        {
                            // Bidirectional
                            enrolledCourse.setParentCourse(course);
                            course.getEnrollment().add(enrolledCourse);

                            return courseRepository.save(course);
                        }
                        else
                        {
                            throw new UpdateCourseException("Course with ID " + course.getCourseId() + " already contains EnrolledCourse with ID " + enrolledCourse.getEnrolledCourseId());
                        }
                    }
                    else
                    {
                        throw new UpdateCourseException("EnrolledCourse ID cannot be null");
                    }
                }
                else
                {
                    throw new UpdateCourseException("EnrolledCourse cannot be null");
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
    public List<Course> getAllCoursesToRecommendWithLimitByAccountId(Long accountId, Integer limit) throws AccountNotFoundException
    {
        accountService.getAccountByAccountId(accountId);

        List<Course> allCoursesToRecommend = courseRepository.findAllCoursesToRecommendWithLimitByAccountId(accountId, limit);

        return allCoursesToRecommend;
    }

    @Override
    public List<Course> getAllCoursesToRecommendByAccountIdAndTagIds(Long accountId, List<Long> tagIds) throws AccountNotFoundException
    {
        accountService.getAccountByAccountId(accountId);

        List<Course> allCoursesToRecommend = courseRepository.findAllCoursesToRecommendByAccountIdAndTagIds(accountId, tagIds);

        return allCoursesToRecommend;
    }

    @Override
    public Double getCourseRating(Long courseId) throws CourseNotFoundException
    {
        getCourseByCourseId(courseId);

        return courseRepository.findCourseRatingByCourseId(courseId);
    }

    @Override
    public BigDecimal getTotalEarningsByCourseId(Long courseId) throws CourseNotFoundException
    {
        BigDecimal totalEarnings = courseRepository.findTotalEarningsByCourseId(courseId);

        if (totalEarnings != null)
        {
            return totalEarnings;
        }
        else
        {
            throw new CourseNotFoundException("Course with ID: " + courseId + " does not exist!");
        }
    }

    @Override
    public BigDecimal getTotalEarningsByCourseIdAndYear(Long courseId, Integer year) throws CourseNotFoundException
    {
        BigDecimal totalEarnings = courseRepository.findTotalEarningsByCourseIdAndYear(courseId, year);

        if (totalEarnings != null)
        {
            return totalEarnings;
        }
        else
        {
            throw new CourseNotFoundException("Course with ID: " + courseId + " does not exist!");
        }
    }

    @Override
    public Long toggleEnrollmentActiveStatus(Long courseIdToToggle, Long requestingAccountId) throws AccountNotFoundException, CourseNotFoundException, AccountPermissionDeniedException
    {

        Account requestingAccount = accountService.getAccountByAccountId(requestingAccountId);
        Course courseToToggle = getCourseByCourseId(courseIdToToggle);
        Account tutorOfCourse = accountService.getAccountByCourseId(courseIdToToggle);

        // If requesting account is admin or is the tutor of the course, allow deletion
        if (requestingAccount.getIsAdmin() || requestingAccount.getAccountId() == tutorOfCourse.getAccountId())
        {
            // Toggle
            courseToToggle.setIsEnrollmentActive(!courseToToggle.getIsEnrollmentActive());
            return courseRepository.saveAndFlush(courseToToggle).getCourseId();
        }
        else
        {
            throw new AccountPermissionDeniedException("You do not have the rights to toggle enrollment status of this course");
        }
    }
}