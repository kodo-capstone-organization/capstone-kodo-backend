package com.spring.kodo.service.inter;

import com.spring.kodo.entity.EnrolledContent;
import com.spring.kodo.entity.EnrolledCourse;
import com.spring.kodo.entity.EnrolledLesson;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface EnrolledLessonService
{
    EnrolledLesson createNewEnrolledLesson(Long parentLessonId) throws InputDataValidationException, UnknownPersistenceException, CreateNewEnrolledLessonException, LessonNotFoundException;

    EnrolledLesson getEnrolledLessonByEnrolledLessonId(Long enrolledLessonId) throws EnrolledLessonNotFoundException;

    EnrolledLesson getEnrolledLessonByEnrolledContentId(Long enrolledContentId) throws EnrolledLessonNotFoundException;

    EnrolledLesson getEnrolledLessonByStudentIdAndLessonId(Long studentId, Long lessonId) throws EnrolledLessonNotFoundException;

    List<EnrolledLesson> getAllEnrolledLessons();

    List<EnrolledLesson> getAllEnrolledLessonsByParentLessonId(Long parentLessonId);

    EnrolledLesson addEnrolledContentToEnrolledLesson(EnrolledLesson enrolledLesson, EnrolledContent enrolledContent) throws UpdateEnrolledLessonException, EnrolledLessonNotFoundException, EnrolledContentNotFoundException;

    EnrolledLesson checkDateTimeOfCompletionOfEnrolledLessonByEnrolledContentId(Long enrolledContentId) throws EnrolledLessonNotFoundException;
}
