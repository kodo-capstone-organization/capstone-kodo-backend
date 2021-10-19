package com.spring.kodo.unittest;

import com.spring.kodo.entity.Content;
import com.spring.kodo.repository.ContentRepository;
import com.spring.kodo.service.impl.ContentServiceImpl;
import com.spring.kodo.util.exception.ContentNotFoundException;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

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

    @Test
    public void whenGetContentByContentId_thenReturnContent() throws Exception
    {
        // PREPARATION
        Content savedContent = Mockito.mock(Content.class);

        Mockito.when(contentRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedContent));

        // ACTION
        Content retrievedContent = contentServiceImpl.getContentByContentId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedContent.getName(), retrievedContent.getName());
    }

    @Test(expected = ContentNotFoundException.class)
    public void whenGetContentByContentId_thenContentNotFoundException() throws Exception
    {
        // ACTION
        Content retrievedContent = contentServiceImpl.getContentByContentId(ArgumentMatchers.anyLong());
    }
}
