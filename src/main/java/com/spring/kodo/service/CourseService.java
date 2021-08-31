package com.spring.kodo.service;

import com.spring.kodo.entity.*;
import com.spring.kodo.entity.Course;
import com.spring.kodo.util.exception.*;
import com.spring.kodo.util.exception.CourseNotFoundException;

import java.util.List;

public interface CourseService
{
    Course createNewCourse(Course newCourse, Long tutorId, List<String> tagTitles) throws TagNameExistsException, UnknownPersistenceException, InputDataValidationException;

    Course getCourseByCourseId(Long courseId) throws CourseNotFoundException;

    Course getCourseByName(String name) throws CourseNotFoundException;

    List<Course> getAllCourses();

    List<Course> getAllCoursesOfATutor(Long accountId) throws AccountNotFoundException;

    Course addTagToCourse(Course course, String tagTitle) throws CourseNotFoundException, TagNotFoundException, UpdateCourseException, TagNameExistsException, UnknownPersistenceException, InputDataValidationException;

    Course addLessonToCourse(Course course, Lesson lesson) throws CourseNotFoundException, InputDataValidationException, UpdateCourseException;
}
