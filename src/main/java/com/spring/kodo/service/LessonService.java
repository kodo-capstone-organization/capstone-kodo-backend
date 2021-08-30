package com.spring.kodo.service;

import com.spring.kodo.entity.Content;
import com.spring.kodo.entity.Lesson;
import com.spring.kodo.util.exception.CourseNotFoundException;
import com.spring.kodo.util.exception.LessonNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.UpdateContentException;

import java.util.List;

public interface LessonService
{
    Lesson createNewLesson(Lesson newLesson, Long courseId) throws InputDataValidationException, CourseNotFoundException;

    Lesson getLessonByLessonId(Long lessonId) throws LessonNotFoundException;

    Lesson getLessonByName(String name) throws LessonNotFoundException;

    List<Lesson> getAllLessons();

    Lesson updateLesson(Long lessonId, Lesson updatedLesson) throws LessonNotFoundException;

    Boolean deleteLesson(Long lessonId) throws LessonNotFoundException;

    Lesson addContentToLesson(Lesson lesson, Content content) throws LessonNotFoundException, UpdateContentException;
}
