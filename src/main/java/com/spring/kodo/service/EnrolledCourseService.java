package com.spring.kodo.service;

import com.spring.kodo.entity.EnrolledCourse;
import com.spring.kodo.util.exception.EnrolledCourseNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;

import java.util.List;

public interface EnrolledCourseService
{
    EnrolledCourse createNewEnrolledCourse(EnrolledCourse enrolledCourse) throws InputDataValidationException;

    EnrolledCourse getEnrolledCourseByEnrolledCourseId(Long enrolledCourseId) throws EnrolledCourseNotFoundException;

    List<EnrolledCourse> getAllEnrolledCourses();
}
