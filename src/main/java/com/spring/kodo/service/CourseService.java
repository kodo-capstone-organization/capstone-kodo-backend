package com.spring.kodo.service;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.Tag;
import com.spring.kodo.util.exception.*;
import com.spring.kodo.util.exception.CourseNotFoundException;

import java.util.List;

public interface CourseService
{
    Course createNewCourse(Course newCourse, Account tutor, List<String> tagTitles) throws InputDataValidationException;

    Course getCourseByCourseId(Long courseId) throws CourseNotFoundException;

    Course getCourseByName(String name) throws CourseNotFoundException;

    List<Course> getAllCourses();
}
