package com.spring.kodo.unittest;

import com.spring.kodo.entity.*;
import com.spring.kodo.repository.ContentRepository;
import com.spring.kodo.repository.EnrolledLessonRepository;
import com.spring.kodo.service.impl.ContentServiceImpl;
import com.spring.kodo.service.impl.EnrolledLessonServiceImpl;
import com.spring.kodo.service.inter.LessonService;
import com.spring.kodo.util.enumeration.MultimediaType;
import com.spring.kodo.util.exception.ContentNotFoundException;
import com.spring.kodo.util.exception.EnrolledLessonNotFoundException;
import com.spring.kodo.util.exception.LessonNotFoundException;
import org.junit.After;
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

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;


@RunWith(MockitoJUnitRunner.class)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class EnrolledLessonServiceImplUnitTest
{
    @InjectMocks
    private EnrolledLessonServiceImpl enrolledLessonServiceImpl;

    @Mock
    private EnrolledLessonRepository enrolledLessonRepository;

    @Mock
    private LessonService lessonService;

    private EnrolledLesson savedEnrolledLesson;
    private EnrolledLesson unsavedEnrolledLesson;

    private Lesson savedLesson;

    @Before
    public void setup()
    {
        savedLesson = new Lesson(1L, "Lesson 1", "Lesson 1 Description", 1);

        savedEnrolledLesson = new EnrolledLesson(1L);
        unsavedEnrolledLesson = new EnrolledLesson();

        savedEnrolledLesson.setParentLesson(savedLesson);
        unsavedEnrolledLesson.setParentLesson(savedLesson);
    }

    @Test
    public void whenCreateNewEnrolledLesson_thenReturnEnrolledLesson() throws Exception
    {
        // PREPARATION
        Mockito.when(lessonService.getLessonByLessonId(ArgumentMatchers.anyLong())).thenReturn(savedLesson);

        // ACTION
        EnrolledLesson retrievedEnrolledLesson = enrolledLessonServiceImpl.createNewEnrolledLesson(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedEnrolledLesson.getParentLesson(), retrievedEnrolledLesson.getParentLesson());
    }

    @Test(expected = LessonNotFoundException.class)
    public void whenCreateNewEnrolledLesson_then() throws Exception
    {
        Mockito.when(lessonService.getLessonByLessonId(ArgumentMatchers.anyLong())).thenThrow(LessonNotFoundException.class);

        // ACTION
        EnrolledLesson retrievedEnrolledLesson = enrolledLessonServiceImpl.createNewEnrolledLesson(ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetEnrolledLessonByEnrolledLessonId_thenReturnEnrolledLesson() throws Exception
    {
        Mockito.when(enrolledLessonRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedEnrolledLesson));

        // ACTION
        EnrolledLesson retrievedEnrolledLesson = enrolledLessonServiceImpl.getEnrolledLessonByEnrolledLessonId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedEnrolledLesson.getParentLesson(), retrievedEnrolledLesson.getParentLesson());
    }

    @Test(expected = EnrolledLessonNotFoundException.class)
    public void whenGetEnrolledLessonByEnrolledLessonId_thenEnrolledLessonNotFoundException() throws Exception
    {
        // ACTION
        EnrolledLesson retrievedEnrolledLesson = enrolledLessonServiceImpl.getEnrolledLessonByEnrolledLessonId(ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetEnrolledLessonByEnrolledContentId_thenReturnEnrolledLesson() throws Exception
    {
        Mockito.when(enrolledLessonRepository.findByEnrolledContentId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedEnrolledLesson));

        // ACTION
        EnrolledLesson retrievedEnrolledLesson = enrolledLessonServiceImpl.getEnrolledLessonByEnrolledContentId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedEnrolledLesson.getParentLesson(), retrievedEnrolledLesson.getParentLesson());
    }

    @Test(expected = EnrolledLessonNotFoundException.class)
    public void whenGetEnrolledLessonByEnrolledContentId_thenEnrolledLessonNotFoundException() throws Exception
    {
        // ACTION
        EnrolledLesson retrievedEnrolledLesson = enrolledLessonServiceImpl.getEnrolledLessonByEnrolledContentId(ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetEnrolledLessonByStudentIdAndLessonId_thenReturnEnrolledLesson() throws Exception
    {
        Mockito.when(enrolledLessonRepository.findByStudentIdAndLessonId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedEnrolledLesson));

        // ACTION
        EnrolledLesson retrievedEnrolledLesson = enrolledLessonServiceImpl.getEnrolledLessonByStudentIdAndLessonId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedEnrolledLesson.getParentLesson(), retrievedEnrolledLesson.getParentLesson());
    }

    @Test(expected = EnrolledLessonNotFoundException.class)
    public void whenGetEnrolledLessonByStudentIdAndLessonId_thenEnrolledLessonNotFoundException() throws Exception
    {
        // ACTION
        EnrolledLesson retrievedEnrolledLesson = enrolledLessonServiceImpl.getEnrolledLessonByStudentIdAndLessonId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());
    }
}
