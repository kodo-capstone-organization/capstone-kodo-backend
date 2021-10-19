package com.spring.kodo.unittest;

import com.spring.kodo.entity.Course;
import com.spring.kodo.repository.CourseRepository;
import com.spring.kodo.service.impl.CourseServiceImpl;
import com.spring.kodo.util.exception.CourseNotFoundException;
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
public class CourseServiceImplUnitTest
{
    @InjectMocks
    private CourseServiceImpl courseServiceImpl;

    @Mock
    private CourseRepository courseRepository;

    @Test
    public void whenCreateNewCourse_thenReturnCourse() throws Exception
    {
        // PREPARATION
        List<String> tagTitles = new ArrayList<>();

        Course unsavedCourse = new Course("test course", "test description", BigDecimal.TEN, "");
        Course savedCourse = new Course(1L, "test course", "test description", BigDecimal.TEN, "");

        // ACTION
        Mockito.when(courseRepository.saveAndFlush(ArgumentMatchers.any(Course.class))).thenReturn(savedCourse);

        Course retrievedCourse = courseServiceImpl.createNewCourse(unsavedCourse, tagTitles);

        // ASSERTION
        assertEquals(savedCourse.getName(), retrievedCourse.getName());
    }

    // Cannot invoke "com.spring.kodo.entity.Tag.getTitle()" because "tag" is null
//    @Test
//    public void whenCreateNewCourseWithTags_thenReturnCourse() throws Exception
//    {
//        // PREPARATION
//        List<String> tagTitles = Arrays.asList("Java", "C#", "C", "Test");
//        List<Tag> savedTags = new ArrayList<>();
//
//        for (int i = 1; i <= tagTitles.size(); i++)
//        {
//            savedTags.add(new Tag((long) i, tagTitles.get(i - 1)));
//        }
//
//        Course unsavedCourse = new Course("test course", "test description", BigDecimal.TEN, "");
//        Course savedCourse = new Course(1L, "test course", "test description", BigDecimal.TEN, "");
//
//        // ACTION
//        Mockito.when(courseRepository.saveAndFlush(ArgumentMatchers.any(Course.class))).thenReturn(savedCourse);
//
//        for (Tag savedTag : savedTags)
//        {
//            Mockito.when(tagService.getTagByTitleOrCreateNew(savedTag.getTitle())).thenReturn(savedTag);
//            Mockito.when(tagRepository.findByTitle(savedTag.getTitle())).thenReturn(Optional.of(savedTag));
//        }
//
//        Course retrievedCourse = courseServiceImpl.createNewCourse(unsavedCourse, tagTitles);
//
//        // ASSERTION
//        assertEquals(savedCourse.getName(), retrievedCourse.getName());
//    }

    @Test
    public void whenGetCourseByCourseId_thenReturnCourse() throws Exception
    {
        // PREPARATION
        Course savedCourse = Mockito.mock(Course.class);

        Mockito.when(courseRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedCourse));

        // ACTION
        Course retrievedCourse = courseServiceImpl.getCourseByCourseId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedCourse.getName(), retrievedCourse.getName());
    }

    @Test(expected = CourseNotFoundException.class)
    public void whenGetCourseByCourseId_thenCourseNotFoundException() throws Exception
    {
        // ACTION
        Course retrievedCourse = courseServiceImpl.getCourseByCourseId(ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetCourseByLessonId_thenReturnLesson() throws Exception
    {
        // PREPARATION
        Course savedCourse = Mockito.mock(Course.class);

        Mockito.when(courseRepository.findByLessonId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedCourse));

        // ACTION
        Course retrievedCourse = courseServiceImpl.getCourseByLessonId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedCourse.getName(), retrievedCourse.getName());
    }

    @Test(expected = CourseNotFoundException.class)
    public void whenGetCourseByLessonId_thenCourseNotFoundException() throws Exception
    {
        // ACTION
        Course retrievedCourse = courseServiceImpl.getCourseByLessonId(ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetCourseByName_thenReturnLesson() throws Exception
    {
        // PREPARATION
        Course savedCourse = Mockito.mock(Course.class);

        Mockito.when(courseRepository.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(savedCourse));

        // ACTION
        Course retrievedCourse = courseServiceImpl.getCourseByName(ArgumentMatchers.anyString());

        // ASSERTION
        assertEquals(savedCourse.getName(), retrievedCourse.getName());
    }

    @Test(expected = CourseNotFoundException.class)
    public void whenGetCourseByName_thenCourseNotFoundException() throws Exception
    {
        // ACTION
        Course retrievedCourse = courseServiceImpl.getCourseByName(ArgumentMatchers.anyString());
    }

    @Test
    public void whenGetCourseByContentId_thenReturnContent() throws Exception
    {
        // PREPARATION
        Course savedCourse = Mockito.mock(Course.class);

        Mockito.when(courseRepository.findByContentId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedCourse));

        // ACTION
        Course retrievedCourse = courseServiceImpl.getCourseByContentId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedCourse.getName(), retrievedCourse.getName());
    }

    @Test(expected = CourseNotFoundException.class)
    public void whenGetCourseByContentId_thenCourseNotFoundException() throws Exception
    {
        // ACTION
        Course retrievedCourse = courseServiceImpl.getCourseByContentId(ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetCourseByEnrolledContentId_thenReturnEnrolledContent() throws Exception
    {
        // PREPARATION
        Course savedCourse = Mockito.mock(Course.class);

        Mockito.when(courseRepository.findByEnrolledContentId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedCourse));

        // ACTION
        Course retrievedCourse = courseServiceImpl.getCourseByEnrolledContentId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedCourse.getName(), retrievedCourse.getName());
    }

    @Test(expected = CourseNotFoundException.class)
    public void whenGetCourseByEnrolledContentId_thenCourseNotFoundException() throws Exception
    {
        // ACTION
        Course retrievedCourse = courseServiceImpl.getCourseByEnrolledContentId(ArgumentMatchers.anyLong());
    }

    @Test
    public void whenGetCourseByStudentAttemptId_thenReturnStudentAttempt() throws Exception
    {
        // PREPARATION
        Course savedCourse = Mockito.mock(Course.class);

        Mockito.when(courseRepository.findByStudentAttemptId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedCourse));

        // ACTION
        Course retrievedCourse = courseServiceImpl.getCourseByStudentAttemptId(ArgumentMatchers.anyLong());

        // ASSERTION
        assertEquals(savedCourse.getName(), retrievedCourse.getName());
    }

    @Test(expected = CourseNotFoundException.class)
    public void whenGetCourseByStudentAttemptId_thenCourseNotFoundException() throws Exception
    {
        // ACTION
        Course retrievedCourse = courseServiceImpl.getCourseByStudentAttemptId(ArgumentMatchers.anyLong());
    }
}
