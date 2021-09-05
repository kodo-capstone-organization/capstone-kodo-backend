package com.spring.kodo.service.inter;

import com.spring.kodo.entity.EnrolledLesson;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface EnrolledLessonService
{
    EnrolledLesson createNewEnrolledLesson(Long parentLessonId) throws InputDataValidationException, UnknownPersistenceException, CreateNewCompletedLessonException, LessonNotFoundException;

    EnrolledLesson getEnrolledLessonByEnrolledLessonId(Long enrolledLessonId) throws CompletedLessonNotFoundException;

    List<EnrolledLesson> getAllEnrolledLessons();
}
