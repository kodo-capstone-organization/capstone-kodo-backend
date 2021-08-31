package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.EnrolledCourse;
import com.spring.kodo.entity.Tag;
import com.spring.kodo.repository.EnrolledCourseRepository;
import com.spring.kodo.service.AccountService;
import com.spring.kodo.service.CourseService;
import com.spring.kodo.service.EnrolledCourseService;
import com.spring.kodo.util.MessageFormatterUtil;
import com.spring.kodo.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    public EnrolledCourse createNewEnrolledCourse(Long studentId, Long courseId) throws InputDataValidationException, CourseNotFoundException, AccountNotFoundException, CreateNewEnrolledCourseException
    {
        EnrolledCourse newEnrolledCourse = new EnrolledCourse();
        Set<ConstraintViolation<EnrolledCourse>> constraintViolations = validator.validate(newEnrolledCourse);
        if (constraintViolations.isEmpty())
        {
            Course course = courseService.getCourseByCourseId(courseId);
            Account student = accountService.getAccountByAccountId(studentId);

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
            throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
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
}
