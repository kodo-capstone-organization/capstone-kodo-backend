package com.spring.kodo.service.inter;

import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.EnrolledCourse;
import com.spring.kodo.entity.Lesson;
import com.spring.kodo.entity.Tag;
import com.spring.kodo.util.exception.*;

import java.math.BigDecimal;
import java.util.List;

public interface CourseService
{
    Course createNewCourse(Course newCourse, List<String> tagTitles)
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

    Course getCourseByContentId(Long contentId) throws CourseNotFoundException;

    Course getCourseByEnrolledContentId(Long enrolledContentId) throws CourseNotFoundException;

    Course getCourseByStudentAttemptId(Long studentAttemptId) throws CourseNotFoundException;

    Course getCourseByForumCategoryId(Long forumCategoryId) throws CourseNotFoundException;

    Course getCourseByForumThreadId(Long forumThreadId) throws CourseNotFoundException;

    List<Course> getAllCoursesWithActiveEnrollment();

    List<Course> getCoursesPendingRequest();

    List<Course> getAllCourses();

    List<Course> getAllCoursesByTagTitle(String tagTitle);

    List<Course> getAllCoursesByTagId(Long tagId);

    List<Course> getAllCoursesByKeyword(String keyword) throws CourseWithKeywordNotFoundException;

    List<Course> getAllCoursesByTutorId(Long tutorId) throws AccountNotFoundException;

    Course addEnrolledCourseToCourse(Course course, EnrolledCourse enrolledCourse) throws CourseNotFoundException, UpdateCourseException, EnrolledCourseNotFoundException;

    List<Course> getAllCoursesToRecommendWithLimitByAccountId(Long accountId, Integer limit) throws AccountNotFoundException;

    List<Course> getAllCoursesToRecommendByAccountIdAndTagIds(Long accountId, List<Long> tagIds) throws AccountNotFoundException;

    List<Course> getAllCoursesInTheLast14Days();

    List<Course> getAllCoursesThatArePopular() throws CourseNotFoundException;

    Course updateCourse(
            Course course,
            List<Long> enrolledCourseIds,
            List<Long> lessonIds,
            List<String> courseTagTitles
    ) throws InputDataValidationException, CourseNotFoundException, UpdateCourseException, TagNameExistsException, UnknownPersistenceException, TagNotFoundException, LessonNotFoundException, EnrolledCourseNotFoundException;

    Course addTagToCourse(Course course, String tagTitle) throws CourseNotFoundException, TagNotFoundException, UpdateCourseException, TagNameExistsException, UnknownPersistenceException, InputDataValidationException;

    Course addTagToCourse(Course course, Tag tag) throws UpdateCourseException, CourseNotFoundException, TagNotFoundException;

    Course addLessonToCourse(Course course, Lesson lesson) throws UpdateCourseException, CourseNotFoundException, LessonNotFoundException;

    Double getCourseRatingByCourseId(Long courseId) throws CourseNotFoundException;

    BigDecimal getTotalEarningsByCourseId(Long courseId) throws CourseNotFoundException;

    BigDecimal getTotalEarningsByCourseIdAndYear(Long courseId, Integer year) throws CourseNotFoundException;

    Long toggleEnrollmentActiveStatus(Long courseIdToToggle, Long requestingAccountId) throws AccountNotFoundException, CourseNotFoundException, AccountPermissionDeniedException;

    Long toggleReviewRequestStatus(Long courseIdToToggle, Long requestingAccountId) throws AccountNotFoundException, CourseNotFoundException, AccountPermissionDeniedException;

    Integer getNumberOfNewCourseForCurrentMonth();

    Integer getNumberOfNewCoursesForPreviousMonth();
}
