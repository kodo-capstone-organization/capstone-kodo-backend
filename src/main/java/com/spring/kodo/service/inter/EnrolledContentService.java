package com.spring.kodo.service.inter;

import com.spring.kodo.entity.EnrolledContent;
import com.spring.kodo.entity.EnrolledCourse;
import com.spring.kodo.entity.StudentAttempt;
import com.spring.kodo.util.exception.*;

import java.time.LocalDateTime;
import java.util.List;

public interface EnrolledContentService
{
    EnrolledContent createNewEnrolledContent(Long parentContentId) throws InputDataValidationException, UnknownPersistenceException, CreateNewEnrolledContentException, ContentNotFoundException;

    EnrolledContent getEnrolledContentByEnrolledContentId(Long enrolledContentId) throws EnrolledContentNotFoundException;

    EnrolledContent getEnrolledContentByAccountIdAndContentId(Long accountId, Long contentId) throws EnrolledContentNotFoundException;

    List<EnrolledContent> getAllEnrolledContents();

    EnrolledContent addStudentAttemptToEnrolledContent(EnrolledContent enrolledContent, StudentAttempt studentAttempt) throws UpdateEnrolledContentException, EnrolledContentNotFoundException, StudentAttemptNotFoundException;

    EnrolledContent setDateTimeOfCompletionOfEnrolledContentByEnrolledContentId(boolean complete, Long enrolledContentId) throws EnrolledContentNotFoundException, EnrolledLessonNotFoundException, EnrolledCourseNotFoundException;

    EnrolledContent setFakeDateTimeOfCompletionOfEnrolledContentByEnrolledContentId(LocalDateTime dateTimeOfCompletion, Long enrolledContentId) throws EnrolledContentNotFoundException, EnrolledLessonNotFoundException, EnrolledCourseNotFoundException, InvalidDateTimeOfCompletionException;

    EnrolledContent setDateTimeOfCompletionOfEnrolledContentByAccountIdAndContentId(boolean complete, Long accountId, Long contentId) throws EnrolledContentNotFoundException, EnrolledLessonNotFoundException, EnrolledCourseNotFoundException;
}
