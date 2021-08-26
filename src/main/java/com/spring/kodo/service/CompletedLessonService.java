package com.spring.kodo.service;

import com.spring.kodo.entity.CompletedLesson;
import com.spring.kodo.util.exception.CompletedLessonNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.LessonNotFoundException;

import java.util.List;

public interface CompletedLessonService
{
    CompletedLesson createNewCompletedLesson(CompletedLesson completedLesson) throws InputDataValidationException;

    CompletedLesson getCompletedLessonByCompletedLessonId(Long completedLessonId) throws CompletedLessonNotFoundException;

    List<CompletedLesson> getAllCompletedLessons();
}
