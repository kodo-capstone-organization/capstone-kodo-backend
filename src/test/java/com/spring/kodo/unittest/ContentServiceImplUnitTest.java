package com.spring.kodo.unittest;

import com.spring.kodo.entity.Content;
import com.spring.kodo.entity.Multimedia;
import com.spring.kodo.entity.Quiz;
import com.spring.kodo.repository.ContentRepository;
import com.spring.kodo.service.impl.ContentServiceImpl;
import com.spring.kodo.util.enumeration.MultimediaType;
import com.spring.kodo.util.exception.ContentNotFoundException;
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
public class ContentServiceImplUnitTest
{
    @InjectMocks
    private ContentServiceImpl contentServiceImpl;

    @Mock
    private ContentRepository contentRepository;

    private Content savedMultimedia;
    private Content savedQuiz;

    @Before
    public void setUp()
    {
        savedMultimedia = new Multimedia(1L, "Lecture 1 Multimedia", "Lecture 1 Multimedia", "https://www.youtube.com/watch?v=7FJQ0TdsMxI", MultimediaType.VIDEO);
        savedQuiz = new Quiz(2L, "Lecture 1 Quiz", "Lecture 1 Quiz", LocalTime.of(1, 0, 0), 100);
    }

    @Test
    public void whenGetContentByContentId_thenReturnMultimedia() throws Exception
    {
        Mockito.when(contentRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedMultimedia));

        // ACTION
        Content retrievedContent = contentServiceImpl.getContentByContentId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedMultimedia.getName(), retrievedContent.getName());
    }

    @Test
    public void whenGetContentByContentId_thenReturnQuiz() throws Exception
    {
        Mockito.when(contentRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedQuiz));

        // ACTION
        Content retrievedContent = contentServiceImpl.getContentByContentId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedQuiz.getName(), retrievedContent.getName());
    }

    @Test(expected = ContentNotFoundException.class)
    public void whenGetContentByContentId_thenContentNotFoundException() throws Exception
    {
        // ACTION
        Content retrievedContent = contentServiceImpl.getContentByContentId(ArgumentMatchers.anyLong());
    }
}
