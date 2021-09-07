package com.spring.kodo.service.inter;

import com.spring.kodo.entity.Content;
import com.spring.kodo.entity.Lesson;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface LessonService
{
    Lesson createNewLesson(Lesson newLesson) throws InputDataValidationException, CourseNotFoundException, UnknownPersistenceException;

    Lesson getLessonByLessonId(Long lessonId) throws LessonNotFoundException;

    Lesson getLessonByName(String name) throws LessonNotFoundException;

    Lesson getLessonByContentId(Long contentId) throws LessonNotFoundException;

    List<Lesson> getAllLessons();

    Lesson updateLesson(Lesson lesson, List<Long> contentIds) throws LessonNotFoundException, UpdateContentException, UnknownPersistenceException, ContentNotFoundException, InputDataValidationException;

    Boolean deleteLesson(Long lessonId) throws LessonNotFoundException, CourseNotFoundException, TagNotFoundException, EnrolledCourseNotFoundException, UpdateCourseException, TagNameExistsException, InputDataValidationException, UnknownPersistenceException;

    Lesson addContentToLesson(Lesson lesson, Content content) throws LessonNotFoundException, UpdateContentException, UnknownPersistenceException, ContentNotFoundException;
}
