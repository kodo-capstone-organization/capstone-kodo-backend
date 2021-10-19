package com.spring.kodo.unittest;

import com.spring.kodo.entity.Tag;
import com.spring.kodo.repository.TagRepository;
import com.spring.kodo.service.impl.TagServiceImpl;
import com.spring.kodo.util.exception.DeleteTagException;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.TagNotFoundException;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class TagServiceImplUnitTest
{
    @InjectMocks
    private TagServiceImpl tagServiceImpl;

    @Mock
    private TagRepository tagRepository;

    @Test
    public void whenCreateNewTag_thenReturnTag() throws Exception
    {
        // PREPARATION
        Tag unsavedTag = new Tag("new tag");
        Tag savedTag = new Tag(1L, "new tag");

        Mockito.when(tagRepository.saveAndFlush(ArgumentMatchers.any(Tag.class))).thenReturn(savedTag);

        // ACTION
        Tag retrievedTag = tagServiceImpl.createNewTag(unsavedTag);

        // ASSERTION
        assertEquals(savedTag.getTitle(), retrievedTag.getTitle());
    }

    @Test(expected = InputDataValidationException.class)
    public void whenCreateNewTagWithEmptyTitle_thenInputDataValidationException() throws Exception
    {
        // PREPARATION
        Tag unsavedTag = new Tag();

        // ACTION
        tagServiceImpl.createNewTag(unsavedTag);
    }

    @Test
    public void whenGetTagByTagId_thenReturnTag() throws Exception
    {
        // PREPARATION
        Tag savedTag = Mockito.mock(Tag.class);

        Mockito.when(tagRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(savedTag));

        // ACTION
        Tag retrievedTag = tagServiceImpl.getTagByTagId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedTag.getTitle(), retrievedTag.getTitle());
    }

    @Test(expected = TagNotFoundException.class)
    public void whenGetTagByTagIdWithInvalidTagId_thenTagNotFoundException() throws Exception
    {
        // PREPARATION
        Mockito.when(tagRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        // ASSERTION
        tagServiceImpl.getTagByTagId(ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetTagByTitle_thenReturnTag() throws Exception
    {
        // PREPARATION
        Tag savedTag = Mockito.mock(Tag.class);

        Mockito.when(tagRepository.findByTitle(ArgumentMatchers.anyString())).thenReturn(Optional.of(savedTag));

        // ACTION
        Tag retrievedTag = tagServiceImpl.getTagByTitle(ArgumentMatchers.anyString());

        // ASSERTION
        assertEquals(savedTag.getTitle(), retrievedTag.getTitle());
    }

    @Test(expected = TagNotFoundException.class)
    public void whenGetTagByTitleWithInvalidTitle_thenTagNotFoundException() throws Exception
    {
        // PREPARATION
        Mockito.when(tagRepository.findByTitle(ArgumentMatchers.anyString())).thenReturn(Optional.empty());

        // ASSERTION
        tagServiceImpl.getTagByTitle(ArgumentMatchers.anyString());
    }

    @Test(expected = DeleteTagException.class)
    public void whenDeleteTagByTagIdWithNullTagIdValue_thenDeleteTagException() throws Exception
    {
        tagServiceImpl.deleteTagByTagId(null);
    }
}
