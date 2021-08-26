package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.Course;
import com.spring.kodo.repository.CourseRepository;
import com.spring.kodo.service.CourseService;
import com.spring.kodo.util.exception.CourseNotFoundException;
import com.spring.kodo.util.exception.CourseNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService
{
    @Autowired
    private CourseRepository courseRepository;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CourseServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Course createNewCourse(Course course, List<String> tagTitles) throws InputDataValidationException
    {
        return null;
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
}
