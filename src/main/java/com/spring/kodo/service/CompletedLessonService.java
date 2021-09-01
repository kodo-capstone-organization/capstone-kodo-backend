package com.spring.kodo.service;

import com.spring.kodo.entity.CompletedLesson;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface CompletedLessonService
{
    CompletedLesson createNewCompletedLesson(Long parentLessonId) throws InputDataValidationException, UnknownPersistenceException, CreateNewCompletedLessonException, LessonNotFoundException;

    CompletedLesson getCompletedLessonByCompletedLessonId(Long completedLessonId) throws CompletedLessonNotFoundException;

    List<CompletedLesson> getAllCompletedLessons();
}
