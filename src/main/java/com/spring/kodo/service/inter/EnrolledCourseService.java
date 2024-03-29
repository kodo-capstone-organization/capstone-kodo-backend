package com.spring.kodo.service.inter;

import com.spring.kodo.entity.EnrolledLesson;
import com.spring.kodo.entity.EnrolledCourse;
import com.spring.kodo.entity.rest.response.EnrolledCourseWithStudentCompletion;
import com.spring.kodo.entity.rest.response.EnrolledCourseWithStudentResp;
import com.spring.kodo.util.exception.*;

import java.time.LocalDateTime;
import java.util.List;

public interface EnrolledCourseService
{
    EnrolledCourse createNewEnrolledCourse(Long studentId, Long courseId) throws InputDataValidationException, CourseNotFoundException, AccountNotFoundException, CreateNewEnrolledCourseException, UnknownPersistenceException;

    EnrolledCourse getEnrolledCourseByEnrolledCourseId(Long enrolledCourseId) throws EnrolledCourseNotFoundException;

    EnrolledCourse getEnrolledCourseByEnrolledLessonId(Long enrolledLessonId) throws EnrolledCourseNotFoundException;

    EnrolledCourse getEnrolledCourseByEnrolledContentId(Long enrolledContentId) throws EnrolledCourseNotFoundException;

    EnrolledCourse getEnrolledCourseByStudentIdAndCourseName(Long studentId, String courseName) throws EnrolledCourseNotFoundException;

    EnrolledCourse getEnrolledCourseByStudentIdAndCourseId(Long studentId, Long courseId) throws EnrolledCourseNotFoundException;

    List<EnrolledCourse> getAllEnrolledCourses();

    EnrolledCourse setCourseRatingByEnrolledCourseId(Long enrolledCourseId, Integer courseRating) throws EnrolledCourseNotFoundException, InputDataValidationException;

    EnrolledCourse addEnrolledLessonToEnrolledCourse(EnrolledCourse enrolledCourse, EnrolledLesson enrolledLesson) throws UpdateEnrolledCourseException, EnrolledCourseNotFoundException, EnrolledLessonNotFoundException;

    EnrolledCourse checkDateTimeOfCompletionOfEnrolledCourseByEnrolledLessonId(Long enrolledLessonId, LocalDateTime dateTimeOfCompletion) throws EnrolledCourseNotFoundException;

    List<EnrolledCourseWithStudentResp> getAllCompletionPercentagesByCourseId(Long courseId);

    List<EnrolledCourseWithStudentCompletion> getAllEnrolledStudentsCompletion(Long courseId);

    Boolean isEnrolledInCourseByStudentIdAndCourseId(Long studentId, Long courseId);
}
