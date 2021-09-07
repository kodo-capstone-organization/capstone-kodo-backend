package com.spring.kodo.service.inter;

import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.Lesson;
import com.spring.kodo.entity.Tag;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface CourseService
{
    Course createNewCourse(Course newCourse, Long tutorId, List<String> tagTitles)
            throws CreateNewCourseException,
            UpdateCourseException,
            TagNotFoundException,
            AccountNotFoundException,
            CourseNotFoundException,
            TagNameExistsException,
            UnknownPersistenceException,
            InputDataValidationException;

    Course getCourseByCourseId(Long courseId) throws CourseNotFoundException;

    Course getCourseByLessonId(Long lessonId) throws CourseNotFoundException;

    Course getCourseByName(String name) throws CourseNotFoundException;

    List<Course> getAllCourses();

    List<Course> getAllCoursesByTagTitle(String tagTitle) throws TagNotFoundException;

    List<Course> getAllCoursesByKeyword(String keyword) throws CourseWithKeywordNotFoundException;

    List<Course> getAllCoursesByTutorId(Long tutorId) throws AccountNotFoundException;

    List<Course> getAllCoursesToRecommend(Long accountId) throws AccountNotFoundException;

    Course addTagToCourse(Course course, String tagTitle) throws CourseNotFoundException, TagNotFoundException, UpdateCourseException, TagNameExistsException, UnknownPersistenceException, InputDataValidationException;

    Course addTagToCourse(Course course, Tag tag) throws UpdateCourseException, CourseNotFoundException, TagNotFoundException;

    Course addLessonToCourse(Course course, Lesson lesson) throws UpdateCourseException, CourseNotFoundException, LessonNotFoundException;

//    Course addForumCategoryToCourse(Course course, ForumCategory forumCategory) throws UpdateCourseException, CourseNotFoundException, ForumCategoryNotFoundException;
}
