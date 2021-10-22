package com.spring.kodo.unittest;

import com.spring.kodo.entity.ForumCategory;
import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.ForumCategory;
import com.spring.kodo.entity.ForumCategory;
import com.spring.kodo.repository.ForumCategoryRepository;
import com.spring.kodo.service.impl.ForumCategoryServiceImpl;
import com.spring.kodo.service.inter.CourseService;
import com.spring.kodo.util.exception.ForumCategoryNotFoundException;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;


@RunWith(MockitoJUnitRunner.class)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class ForumCategoryServiceImplUnitTest
{
    @InjectMocks
    private ForumCategoryServiceImpl forumCategoryServiceImpl;

    @Mock
    private ForumCategoryRepository forumCategoryRepository;

    @Mock
    private CourseService courseService;

    private Course savedCourse;

    private ForumCategory savedForumCategory;
    private ForumCategory unsavedForumCategory;

    @Before
    public void setup()
    {
        savedCourse = new Course(1L, "Java Course", "Java Course Description", BigDecimal.TEN, "https://helpx.adobe.com/content/dam/help/en/photos…before_and_after/image-before/Landscape-Color.jpg");

        savedForumCategory = new ForumCategory(1L, "Forum 1", "Forum 1 Description");
        unsavedForumCategory = new ForumCategory("Forum 1", "Forum 1 Description");

        savedForumCategory.setCourse(savedCourse);
        unsavedForumCategory.setCourse(savedCourse);
    }

    @Test
    public void whenCreateNewForumCategory_thenReturnForumCategory() throws Exception
    {
        // PREPARATION
        Mockito.when(forumCategoryRepository.saveAndFlush(ArgumentMatchers.any(ForumCategory.class))).thenReturn(savedForumCategory);
        Mockito.when(courseService.getCourseByCourseId(ArgumentMatchers.anyLong())).thenReturn(savedCourse);

        // ACTION
        ForumCategory retrievedForumCategory = forumCategoryServiceImpl.createNewForumCategory(unsavedForumCategory, ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedForumCategory.getName(), retrievedForumCategory.getName());
    }

    @Test
    public void whenGetForumCategoryByForumCategoryId_thenReturnForumCategory() throws Exception
    {
        // PREPARATION
        Mockito.when(forumCategoryRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedForumCategory));

        // ACTION
        ForumCategory retrievedForumCategory = forumCategoryServiceImpl.getForumCategoryByForumCategoryId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedForumCategory.getName(), retrievedForumCategory.getName());
    }

    @Test(expected = ForumCategoryNotFoundException.class)
    public void whenGetForumCategoryByForumCategoryId_thenForumCategoryNotFoundException() throws Exception
    {
        // ACTION
        ForumCategory retrievedForumCategory = forumCategoryServiceImpl.getForumCategoryByForumCategoryId(ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetAllForumCategories_thenReturnAllForumCategories() throws Exception
    {
        // PREPARATION
        int forumCategoriesSize = 3;

        List<ForumCategory> savedForumCategories = new ArrayList<>();
        for (int i = 0; i < forumCategoriesSize; i++)
        {
            savedForumCategories.add(Mockito.mock(ForumCategory.class));
        }

        Mockito.when(forumCategoryRepository.findAll()).thenReturn(savedForumCategories);

        // ACTION
        List<ForumCategory> retrievedForumCategories = forumCategoryServiceImpl.getAllForumCategories();

        // ASSERTION
        for (int i = 0; i < forumCategoriesSize; i++)
        {
            assertEquals(savedForumCategories.get(i).getForumCategoryId().longValue(), retrievedForumCategories.get(i).getForumCategoryId().longValue());
        }
    }
}
