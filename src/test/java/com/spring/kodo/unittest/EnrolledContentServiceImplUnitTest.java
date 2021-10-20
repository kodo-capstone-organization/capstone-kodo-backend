package com.spring.kodo.unittest;

import com.spring.kodo.entity.*;
import com.spring.kodo.repository.EnrolledContentRepository;
import com.spring.kodo.service.impl.EnrolledContentServiceImpl;
import com.spring.kodo.service.inter.ContentService;
import com.spring.kodo.service.inter.EnrolledCourseService;
import com.spring.kodo.service.inter.EnrolledLessonService;
import com.spring.kodo.util.enumeration.MultimediaType;
import com.spring.kodo.util.exception.EnrolledContentNotFoundException;
import org.junit.Before;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

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

    private Content savedMultimedia;
    private Content savedQuiz;

    private EnrolledContent savedEnrolledContentWithMultimedia;
    private EnrolledContent unsavedEnrolledContentWithMultimedia;
    private EnrolledContent savedEnrolledContentWithQuiz;
    private EnrolledContent unsavedEnrolledContentWithQuiz;
    private EnrolledContent savedEnrolledContentWithDate;
    private EnrolledContent unsavedEnrolledContentWithDate;

    private EnrolledLesson savedEnrolledLesson;
    private EnrolledCourse savedEnrolledCourse;

    @Before
    public void setup()
    {
        savedMultimedia = new Multimedia(1L, "Lecture 1 Multimedia", "Lecture 1 Multimedia", "https://www.youtube.com/watch?v=7FJQ0TdsMxI", MultimediaType.VIDEO);
        savedQuiz = new Quiz(1L, "Lecture 1 Quiz", "Lecture 1 Quiz", LocalTime.of(1, 0, 0), 100);

        savedEnrolledContentWithMultimedia = new EnrolledContent(1L);
        unsavedEnrolledContentWithMultimedia = new EnrolledContent();

        savedEnrolledContentWithMultimedia.setParentContent(savedMultimedia);
        unsavedEnrolledContentWithMultimedia.setParentContent(savedMultimedia);

        savedEnrolledContentWithQuiz = new EnrolledContent(1L);
        unsavedEnrolledContentWithQuiz = new EnrolledContent();

        savedEnrolledContentWithQuiz.setParentContent(savedQuiz);
        unsavedEnrolledContentWithQuiz.setParentContent(savedQuiz);

        savedEnrolledContentWithDate = new EnrolledContent(1L);
        unsavedEnrolledContentWithDate = new EnrolledContent();

        savedEnrolledContentWithDate.setDateTimeOfCompletion(LocalDateTime.now());
        unsavedEnrolledContentWithDate.setDateTimeOfCompletion(LocalDateTime.now());

        savedEnrolledLesson = new EnrolledLesson(1L);
        savedEnrolledCourse = new EnrolledCourse(1L, 5);
    }

    @Test
    public void whenCreateNewEnrolledContentWithMultimedia_thenReturnEnrolledContentWithMultimedia() throws Exception
    {
        // PREPARATION
        Mockito.when(enrolledContentRepository.saveAndFlush(ArgumentMatchers.any(EnrolledContent.class))).thenReturn(savedEnrolledContentWithMultimedia);
        Mockito.when(contentService.getContentByContentId(ArgumentMatchers.anyLong())).thenReturn(savedMultimedia);

        // ACTION
        EnrolledContent retrievedEnrolledContent = enrolledContentServiceImpl.createNewEnrolledContent(1L);

        // ASSERTION
        assertEquals(savedEnrolledContentWithMultimedia.getDateTimeOfCompletion(), retrievedEnrolledContent.getDateTimeOfCompletion());
    }

    @Test
    public void whenCreateNewEnrolledContentWithQuiz_thenReturnEnrolledContentWithQuiz() throws Exception
    {
        // PREPARATION
        Mockito.when(enrolledContentRepository.saveAndFlush(ArgumentMatchers.any(EnrolledContent.class))).thenReturn(savedEnrolledContentWithQuiz);
        Mockito.when(contentService.getContentByContentId(ArgumentMatchers.anyLong())).thenReturn(savedQuiz);

        // ACTION
        EnrolledContent retrievedEnrolledContent = enrolledContentServiceImpl.createNewEnrolledContent(1L);

        // ASSERTION
        assertEquals(savedEnrolledContentWithQuiz.getDateTimeOfCompletion(), retrievedEnrolledContent.getDateTimeOfCompletion());
    }

    @Test
    public void whenGetEnrolledContentByEnrolledContentId_thenReturnEnrolledContentWithMultimedia() throws Exception
    {
        // PREPARATION
        Mockito.when(enrolledContentRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedEnrolledContentWithMultimedia));

        // ACTION
        EnrolledContent retrievedEnrolledContent = enrolledContentServiceImpl.getEnrolledContentByEnrolledContentId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedEnrolledContentWithMultimedia.getEnrolledContentId(), retrievedEnrolledContent.getEnrolledContentId());
    }

    @Test
    public void whenGetEnrolledContentByEnrolledContentId_thenReturnEnrolledContentWithQuiz() throws Exception
    {
        // PREPARATION
        Mockito.when(enrolledContentRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedEnrolledContentWithQuiz));

        // ACTION
        EnrolledContent retrievedEnrolledContent = enrolledContentServiceImpl.getEnrolledContentByEnrolledContentId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedEnrolledContentWithQuiz.getEnrolledContentId(), retrievedEnrolledContent.getEnrolledContentId());
    }

    @Test(expected = EnrolledContentNotFoundException.class)
    public void whenGetEnrolledContentByEnrolledContentId_thenEnrolledContentNotFoundException() throws Exception
    {
        // ACTION
        EnrolledContent retrievedEnrolledContent = enrolledContentServiceImpl.getEnrolledContentByEnrolledContentId(ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetEnrolledContentByAccountIdAndContentId_thenReturnEnrolledContentWithMultimedia() throws Exception
    {
        // PREPARATION
        Mockito.when(enrolledContentRepository.findByAccountIdAndContentId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedEnrolledContentWithMultimedia));

        // ACTION
        EnrolledContent retrievedEnrolledContent = enrolledContentServiceImpl.getEnrolledContentByAccountIdAndContentId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedEnrolledContentWithMultimedia.getEnrolledContentId(), retrievedEnrolledContent.getEnrolledContentId());
    }

    @Test
    public void whenGetEnrolledContentByAccountIdAndContentId_thenReturnEnrolledContentWithQuiz() throws Exception
    {
        // PREPARATION
        Mockito.when(enrolledContentRepository.findByAccountIdAndContentId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedEnrolledContentWithQuiz));

        // ACTION
        EnrolledContent retrievedEnrolledContent = enrolledContentServiceImpl.getEnrolledContentByAccountIdAndContentId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedEnrolledContentWithQuiz.getEnrolledContentId(), retrievedEnrolledContent.getEnrolledContentId());
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

    @Test
    public void whenSetDateTimeOfCompletionOfEnrolledContentByEnrolledContentId_thenReturnEnrolledContent() throws Exception
    {
        // PREPARATION
        Mockito.when(enrolledContentRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(savedEnrolledContentWithMultimedia));
        Mockito.when(enrolledLessonService.checkDateTimeOfCompletionOfEnrolledLessonByEnrolledContentId(1L, LocalDateTime.now())).thenReturn(savedEnrolledLesson);
        Mockito.when(enrolledCourseService.checkDateTimeOfCompletionOfEnrolledCourseByEnrolledLessonId(1L, LocalDateTime.now())).thenReturn(savedEnrolledCourse);

        // ACTION
        EnrolledContent retrievedEnrolledContent = enrolledContentServiceImpl.setDateTimeOfCompletionOfEnrolledContentByEnrolledContentId(true, ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedEnrolledContentWithDate.getDateTimeOfCompletion().getYear(), retrievedEnrolledContent.getDateTimeOfCompletion().getYear());
        assertEquals(savedEnrolledContentWithDate.getDateTimeOfCompletion().getMonth(), retrievedEnrolledContent.getDateTimeOfCompletion().getMonth());
        assertEquals(savedEnrolledContentWithDate.getDateTimeOfCompletion().getDayOfMonth(), retrievedEnrolledContent.getDateTimeOfCompletion().getDayOfMonth());
    }

    @Test(expected = EnrolledContentNotFoundException.class)
    public void whenSetDateTimeOfCompletionOfEnrolledContentByEnrolledContentId_thenEnrolledContentNotFoundException() throws Exception
    {
        // ACTION
        EnrolledContent retrievedEnrolledContent = enrolledContentServiceImpl.setDateTimeOfCompletionOfEnrolledContentByEnrolledContentId(true, ArgumentMatchers.anyLong());
    }
}
