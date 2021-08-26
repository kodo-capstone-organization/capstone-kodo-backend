package com.spring.kodo.service.impl;

import com.spring.kodo.entity.EnrolledCourse;
import com.spring.kodo.repository.EnrolledCourseRepository;
import com.spring.kodo.service.EnrolledCourseService;
import com.spring.kodo.util.exception.EnrolledCourseNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

@Service
public class EnrolledCourseServiceImpl implements EnrolledCourseService
{
    @Autowired
    private EnrolledCourseRepository enrolledCourseRepository;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public EnrolledCourseServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public EnrolledCourse createNewEnrolledCourse(EnrolledCourse enrolledCourse) throws InputDataValidationException
    {
        return null;
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
