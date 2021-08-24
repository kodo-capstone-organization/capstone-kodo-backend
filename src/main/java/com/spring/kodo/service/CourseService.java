package com.spring.kodo.service;

import com.spring.kodo.entity.Course;
import com.spring.kodo.util.exception.CourseNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;

import java.util.List;

public interface CourseService
{
    Course createNewCourse(Course course, List<String> tagTitles) throws InputDataValidationException;

    List<Course> getAllCourses();
    Course getCourseByName(String name) throws CourseNotFoundException;
}
