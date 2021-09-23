package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.EnrolledCourse;
import com.spring.kodo.entity.EnrolledLesson;
import com.spring.kodo.repository.EnrolledCourseRepository;
import com.spring.kodo.restentity.response.EnrolledCourseWithStudentResp;
import com.spring.kodo.service.inter.AccountService;
import com.spring.kodo.service.inter.CourseService;
import com.spring.kodo.service.inter.EnrolledCourseService;
import com.spring.kodo.service.inter.EnrolledLessonService;
import com.spring.kodo.util.FormatterUtil;
import com.spring.kodo.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class EnrolledCourseServiceImpl implements EnrolledCourseService
{
    @Autowired
    private EnrolledCourseRepository enrolledCourseRepository;

    @Autowired
    private EnrolledLessonService enrolledLessonService;

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
                            if (enrolledCourse.getParentCourse().getCourseId().equals(course.getCourseId()))
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
                throw new InputDataValidationException(FormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
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
    public EnrolledCourse getEnrolledCourseByEnrolledLessonId(Long enrolledLessonId) throws EnrolledCourseNotFoundException
    {
        EnrolledCourse enrolledCourse = enrolledCourseRepository.findByEnrolledLessonId(enrolledLessonId).orElse(null);

        if (enrolledCourse != null)
        {
            return enrolledCourse;
        }
        else
        {
            throw new EnrolledCourseNotFoundException("EnrolledLesson with ID: " + enrolledLessonId + " does not exist!");
        }
    }

    @Override
    public EnrolledCourse getEnrolledCourseByStudentIdAndCourseName(Long studentId, String courseName) throws EnrolledCourseNotFoundException
    {
        EnrolledCourse enrolledCourse = enrolledCourseRepository.findByStudentIdAndCourseName(studentId, courseName).orElse(null);

        if (enrolledCourse != null)
        {
            return enrolledCourse;
        }
        else
        {
            throw new EnrolledCourseNotFoundException("EnrolledCourse with Account ID " + studentId + " and Course Name: " + courseName + " does not exist!");
        }
    }

    @Override
    public EnrolledCourse getEnrolledCourseByStudentIdAndCourseId(Long studentId, Long courseId) throws EnrolledCourseNotFoundException
    {
        EnrolledCourse enrolledCourse = enrolledCourseRepository.findByStudentIdAndCourseId(studentId, courseId).orElse(null);

        if (enrolledCourse != null)
        {
            return enrolledCourse;
        }
        else
        {
            throw new EnrolledCourseNotFoundException("EnrolledCourse with Account ID " + studentId + " and Course ID: " + courseId + " does not exist!");
        }
    }

    @Override
    public List<EnrolledCourse> getAllEnrolledCourses()
    {
        return enrolledCourseRepository.findAll();
    }

    @Override
    public EnrolledCourse addEnrolledLessonToEnrolledCourse(EnrolledCourse enrolledCourse, EnrolledLesson enrolledLesson) throws UpdateEnrolledCourseException, EnrolledCourseNotFoundException, EnrolledLessonNotFoundException
    {
        if (enrolledCourse != null)
        {
            if (enrolledCourse.getEnrolledCourseId() != null)
            {
                enrolledCourse = getEnrolledCourseByEnrolledCourseId(enrolledCourse.getEnrolledCourseId());
                if (enrolledLesson != null)
                {
                    if (enrolledLesson.getEnrolledLessonId() != null)
                    {
                        enrolledLesson = enrolledLessonService.getEnrolledLessonByEnrolledLessonId(enrolledLesson.getEnrolledLessonId());

                        if (!enrolledCourse.getEnrolledLessons().contains(enrolledLesson))
                        {
                            enrolledCourse.getEnrolledLessons().add(enrolledLesson);

                            enrolledCourseRepository.saveAndFlush(enrolledCourse);
                            return enrolledCourse;
                        }
                        else
                        {
                            throw new UpdateEnrolledCourseException("EnrolledCourse with ID " + enrolledCourse.getEnrolledCourseId() + " already contains EnrolledLesson with ID " + enrolledLesson.getEnrolledLessonId());
                        }
                    }
                    else
                    {
                        throw new UpdateEnrolledCourseException("EnrolledLesson ID cannot be null");
                    }
                }
                else
                {
                    throw new UpdateEnrolledCourseException("EnrolledLesson cannot be null");
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

    @Override
    public EnrolledCourse setCourseRatingByEnrolledCourseId(Long enrolledCourseId, Integer courseRating) throws EnrolledCourseNotFoundException, InputDataValidationException
    {
        EnrolledCourse enrolledCourse = getEnrolledCourseByEnrolledCourseId(enrolledCourseId);

        enrolledCourse.setCourseRating(courseRating);

        Set<ConstraintViolation<EnrolledCourse>> constraintViolations = validator.validate(enrolledCourse);
        if (constraintViolations.isEmpty())
        {
            enrolledCourseRepository.saveAndFlush(enrolledCourse);
            return enrolledCourse;
        }
        else
        {
            throw new InputDataValidationException(FormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public EnrolledCourse checkDateTimeOfCompletionOfEnrolledCourseByEnrolledLessonId(Long enrolledLessonId) throws EnrolledCourseNotFoundException
    {
        EnrolledCourse enrolledCourse = getEnrolledCourseByEnrolledLessonId(enrolledLessonId);

        boolean setDateTimeOfCompletion = true;

        for (EnrolledLesson enrolledLesson : enrolledCourse.getEnrolledLessons())
        {
            if (enrolledLesson.getDateTimeOfCompletion() == null)
            {
                setDateTimeOfCompletion = false;
                break;
            }
        }

        if (setDateTimeOfCompletion)
        {
            enrolledCourse.setDateTimeOfCompletion(LocalDateTime.now());
        }
        else
        {
            enrolledCourse.setDateTimeOfCompletion(null);
        }

        enrolledCourseRepository.saveAndFlush(enrolledCourse);
        return enrolledCourse;
    }

    @Override
    public List<EnrolledCourseWithStudentResp> getAllCompletionPercentagesByCourseId(Long courseId) {
        return enrolledCourseRepository.findAllEnrolledCourseCompletionPercentagesByCourseId(courseId);
    }
}
