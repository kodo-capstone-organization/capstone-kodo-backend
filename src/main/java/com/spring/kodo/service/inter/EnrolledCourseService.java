package com.spring.kodo.service.inter;

import com.spring.kodo.entity.CompletedLesson;
import com.spring.kodo.entity.EnrolledCourse;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface EnrolledCourseService
{
    EnrolledCourse createNewEnrolledCourse(Long studentId, Long courseId) throws InputDataValidationException, CourseNotFoundException, AccountNotFoundException, CreateNewEnrolledCourseException, UnknownPersistenceException;

    EnrolledCourse getEnrolledCourseByEnrolledCourseId(Long enrolledCourseId) throws EnrolledCourseNotFoundException;

    EnrolledCourse getEnrolledCourseByStudentIdAndCourseName(Long studentId, String courseName) throws EnrolledCourseNotFoundException;

    List<EnrolledCourse> getAllEnrolledCourses();

    EnrolledCourse addCompletedLessonToEnrolledCourse(EnrolledCourse enrolledCourse, CompletedLesson completedLesson) throws UpdateEnrolledCourseException, EnrolledCourseNotFoundException, CompletedLessonNotFoundException;
}
