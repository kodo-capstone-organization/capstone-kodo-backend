package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.CompletedLesson;
import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.EnrolledCourse;
import com.spring.kodo.repository.EnrolledCourseRepository;
import com.spring.kodo.service.AccountService;
import com.spring.kodo.service.CompletedLessonService;
import com.spring.kodo.service.CourseService;
import com.spring.kodo.service.EnrolledCourseService;
import com.spring.kodo.util.MessageFormatterUtil;
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

@Service
public class EnrolledCourseServiceImpl implements EnrolledCourseService
{
    @Autowired
    private EnrolledCourseRepository enrolledCourseRepository;

    @Autowired
    private CompletedLessonService completedLessonService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CourseService courseService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public EnrolledCourseServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public EnrolledCourse createNewEnrolledCourse(Long studentId, Long courseId) throws InputDataValidationException, CourseNotFoundException, AccountNotFoundException, CreateNewEnrolledCourseException, UnknownPersistenceException
    {
        try
        {
            EnrolledCourse newEnrolledCourse = new EnrolledCourse();
            Set<ConstraintViolation<EnrolledCourse>> constraintViolations = validator.validate(newEnrolledCourse);
            if (constraintViolations.isEmpty())
            {
                if (studentId != null)
                {
                    Account student = accountService.getAccountByAccountId(studentId);

                    if (courseId != null)
                    {
                        Course course = courseService.getCourseByCourseId(courseId);

                        for (EnrolledCourse enrolledCourse : student.getEnrolledCourses())
                        {
                            if (enrolledCourse.getParentCourse().equals(course))
                            {
                                throw new CreateNewEnrolledCourseException("The student with ID " + studentId + " is already enrolled to course with ID " + courseId);
                            }
                        }

                        newEnrolledCourse.setParentCourse(course);
                        course.getEnrollment().add(newEnrolledCourse);

                        enrolledCourseRepository.saveAndFlush(newEnrolledCourse);
                        return newEnrolledCourse;
                    }
                    else
                    {
                        throw new CreateNewEnrolledCourseException("Course ID cannot be null");
                    }
                }
                else
                {
                    throw new CreateNewEnrolledCourseException("Student ID cannot be null");
                }
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
    public EnrolledCourse getEnrolledCourseByEnrolledCourseId(Long enrolledCourseId) throws EnrolledCourseNotFoundException
    {
        EnrolledCourse enrolledCourse = enrolledCourseRepository.findById(enrolledCourseId).orElse(null);

        if (enrolledCourse != null)
        {
            return enrolledCourse;
        }
        else
        {
            throw new EnrolledCourseNotFoundException("EnrolledCourse with ID: " + enrolledCourseId + " does not exist!");
        }
    }

    @Override
    public List<EnrolledCourse> getAllEnrolledCourses()
    {
        return enrolledCourseRepository.findAll();
    }

    @Override
    public EnrolledCourse addCompletedLessonToEnrolledCourse(EnrolledCourse enrolledCourse, CompletedLesson completedLesson) throws UpdateEnrolledCourseException, EnrolledCourseNotFoundException, CompletedLessonNotFoundException
    {
        if (enrolledCourse != null)
        {
            if (enrolledCourse.getEnrolledCourseId() != null)
            {
                enrolledCourse = getEnrolledCourseByEnrolledCourseId(enrolledCourse.getEnrolledCourseId());
                if (completedLesson != null)
                {
                    if (completedLesson.getCompletedLessonId() != null)
                    {
                        completedLesson = completedLessonService.getCompletedLessonByCompletedLessonId(completedLesson.getCompletedLessonId());

                        if (!enrolledCourse.getCompletedLessons().contains(completedLesson))
                        {
                            enrolledCourse.getCompletedLessons().add(completedLesson);

                            enrolledCourseRepository.saveAndFlush(enrolledCourse);
                            return enrolledCourse;
                        }
                        else
                        {
                            throw new UpdateEnrolledCourseException("EnrolledCourse with ID " + enrolledCourse.getEnrolledCourseId() + " already contains CompletedLesson with ID " + completedLesson.getCompletedLessonId());
                        }
                    }
                    else
                    {
                        throw new UpdateEnrolledCourseException("CompletedLesson ID cannot be null");
                    }
                }
                else
                {
                    throw new UpdateEnrolledCourseException("CompletedLesson cannot be null");
                }
            }
            else
            {
                throw new UpdateEnrolledCourseException("EnrolledCourse ID cannot be null");
            }
        }
        else
        {
            throw new UpdateEnrolledCourseException("EnrolledCourse cannot be null");
        }
    }
}
