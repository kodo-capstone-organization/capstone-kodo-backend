package com.spring.kodo.unittest;

import com.spring.kodo.entity.*;
import com.spring.kodo.repository.ContentRepository;
import com.spring.kodo.repository.EnrolledContentRepository;
import com.spring.kodo.service.impl.EnrolledContentServiceImpl;
import com.spring.kodo.service.inter.ContentService;
import com.spring.kodo.service.inter.EnrolledCourseService;
import com.spring.kodo.service.inter.EnrolledLessonService;
import com.spring.kodo.util.enumeration.MultimediaType;
import com.spring.kodo.util.exception.EnrolledContentNotFoundException;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class EnrolledContentServiceImplUnitTest
{
    @InjectMocks
    private EnrolledContentServiceImpl enrolledContentServiceImpl;

    @Mock
    private EnrolledContentRepository enrolledContentRepository;

    @Mock
    private EnrolledLessonService enrolledLessonService;

    @Mock
    private EnrolledCourseService enrolledCourseService;

    @Mock
    private ContentService contentService;

    @Test
    public void whenCreateNewEnrolledContent_thenReturnEnrolledContent() throws Exception
    {
        // PREPARATION
        EnrolledContent savedEnrolledContent = new EnrolledContent(1L);
        EnrolledContent unsavedEnrolledContent = new EnrolledContent();

        Content savedContent = new Multimedia(1L, "test", "test", "http://test.com", MultimediaType.DOCUMENT);
        savedEnrolledContent.setParentContent(savedContent);
        unsavedEnrolledContent.setParentContent(savedContent);

        // ACTION
        Mockito.when(enrolledContentRepository.saveAndFlush(ArgumentMatchers.any(EnrolledContent.class))).thenReturn(savedEnrolledContent);
        Mockito.when(contentService.getContentByContentId(1L)).thenReturn(savedContent);

        EnrolledContent retrievedEnrolledContent = enrolledContentServiceImpl.createNewEnrolledContent(1L);

        // ASSERTION
        assertEquals(savedEnrolledContent.getDateTimeOfCompletion(), retrievedEnrolledContent.getDateTimeOfCompletion());
    }

    @Test
    public void whenGetEnrolledContentByEnrolledContentId_thenReturnEnrolledContent() throws Exception
    {
        // PREPARATION
        EnrolledContent savedEnrolledContent = Mockito.mock(EnrolledContent.class);

        Mockito.when(enrolledContentRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedEnrolledContent));

        // ACTION
        EnrolledContent retrievedEnrolledContent = enrolledContentServiceImpl.getEnrolledContentByEnrolledContentId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedEnrolledContent.getEnrolledContentId(), retrievedEnrolledContent.getEnrolledContentId());
    }

    @Test(expected = EnrolledContentNotFoundException.class)
    public void whenGetEnrolledContentByEnrolledContentId_thenEnrolledContentNotFoundException() throws Exception
    {
        // ACTION
        EnrolledContent retrievedEnrolledContent = enrolledContentServiceImpl.getEnrolledContentByEnrolledContentId(ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetEnrolledContentByAccountIdAndContentId_thenReturnEnrolledContent() throws Exception
    {
        // PREPARATION
        EnrolledContent savedEnrolledContent = Mockito.mock(EnrolledContent.class);

        Mockito.when(enrolledContentRepository.findByAccountIdAndContentId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedEnrolledContent));

        // ACTION
        EnrolledContent retrievedEnrolledContent = enrolledContentServiceImpl.getEnrolledContentByAccountIdAndContentId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedEnrolledContent.getEnrolledContentId(), retrievedEnrolledContent.getEnrolledContentId());
    }

    @Test(expected = EnrolledContentNotFoundException.class)
    public void whenGetEnrolledContentByAccountIdAndContentId_thenEnrolledContentNotFoundException() throws Exception
    {
        // ACTION
        EnrolledContent retrievedEnrolledContent = enrolledContentServiceImpl.getEnrolledContentByAccountIdAndContentId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetAllEnrolledContents_thenReturnAllEnrolledContents() throws Exception
    {
        // PREPARATION
        int accountsSize = 3;

        List<EnrolledContent> savedEnrolledContents = new ArrayList<>();
        for (int i = 0; i < accountsSize; i++)
        {
            savedEnrolledContents.add(Mockito.mock(EnrolledContent.class));
        }

        Mockito.when(enrolledContentRepository.findAll()).thenReturn(savedEnrolledContents);

        // ACTION
        List<EnrolledContent> retrievedEnrolledContents = enrolledContentServiceImpl.getAllEnrolledContents();

        assertEquals(accountsSize, retrievedEnrolledContents.size());

        // ASSERTION
        for (int i = 0; i < accountsSize; i++)
        {
            assertEquals(savedEnrolledContents.get(i).getEnrolledContentId().longValue(), retrievedEnrolledContents.get(i).getEnrolledContentId().longValue());
        }
    }

//    @Test
//    public void whenSetDateTimeOfCompletionOfEnrolledContentByEnrolledContentId_thenReturnEnrolledContent() throws Exception
//    {
//        // PREPARATION
//        EnrolledContent savedEnrolledContent = new EnrolledContent(1L);
//        EnrolledContent savedEnrolledContentWithDate = new EnrolledContent(1L);
//        savedEnrolledContentWithDate.setDateTimeOfCompletion(LocalDateTime.now());
//
//        EnrolledLesson enrolledLesson = new EnrolledLesson(1L);
//        enrolledLesson.setParentLesson(new Lesson());
//
//        EnrolledCourse enrolledCourse = new EnrolledCourse(1L, 5);
//        enrolledCourse.setParentCourse(new Course());
//
//        // ACTION
//        Mockito.when(enrolledContentRepository.saveAndFlush(ArgumentMatchers.any(EnrolledContent.class))).thenReturn(savedEnrolledContentWithDate);
//        Mockito.when(enrolledContentRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(savedEnrolledContent));
//        Mockito.when(enrolledLessonService.checkDateTimeOfCompletionOfEnrolledLessonByEnrolledContentId(1L, LocalDateTime.now())).thenReturn(enrolledLesson);
//        Mockito.when(enrolledCourseService.checkDateTimeOfCompletionOfEnrolledCourseByEnrolledLessonId(1L, LocalDateTime.now())).thenReturn(enrolledCourse);
//
//        assertNotNull(enrolledLessonService.checkDateTimeOfCompletionOfEnrolledLessonByEnrolledContentId(1L, LocalDateTime.now()));
//        assertNotNull(enrolledLessonService.checkDateTimeOfCompletionOfEnrolledLessonByEnrolledContentId(1L, LocalDateTime.now()).getEnrolledLessonId());
//
//        EnrolledContent retrievedEnrolledContent = enrolledContentServiceImpl.setDateTimeOfCompletionOfEnrolledContentByEnrolledContentId(true, 1L);
//
//        // ASSERTION
//        assertEquals(savedEnrolledContentWithDate.getDateTimeOfCompletion(), retrievedEnrolledContent.getDateTimeOfCompletion());
//    }

    @Test(expected = EnrolledContentNotFoundException.class)
    public void whenSetDateTimeOfCompletionOfEnrolledContentByEnrolledContentId_thenEnrolledContentNotFoundException() throws Exception
    {
        // ACTION
        EnrolledContent retrievedEnrolledContent = enrolledContentServiceImpl.setDateTimeOfCompletionOfEnrolledContentByEnrolledContentId(true, ArgumentMatchers.anyLong());
    }
}
