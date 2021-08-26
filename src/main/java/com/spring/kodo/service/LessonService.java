package com.spring.kodo.service;

import com.spring.kodo.entity.Content;
import com.spring.kodo.entity.Lesson;
import com.spring.kodo.util.exception.LessonNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;

import java.util.List;

public interface LessonService {

    Lesson createNewLesson (Lesson lesson) throws InputDataValidationException;

    Lesson getLessonByLessonId(Long lessonId) throws LessonNotFoundException;

    Lesson getLessonByName(String name) throws LessonNotFoundException;

    Lesson updateLesson(Long lessonId, Lesson updatedLesson) throws LessonNotFoundException;

    Boolean deleteLesson(Long lessonId) throws LessonNotFoundException;
}
