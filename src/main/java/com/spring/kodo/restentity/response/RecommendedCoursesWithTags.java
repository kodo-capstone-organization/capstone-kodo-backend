package com.spring.kodo.restentity.response;

import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.Tag;

import java.util.List;

public class RecommendedCoursesWithTags
{
    private List<Course> courses;
    private List<Tag> tags;

    public RecommendedCoursesWithTags()
    {
    }

    public RecommendedCoursesWithTags(List<Course> courses, List<Tag> tags)
    {
        this.courses = courses;
        this.tags = tags;
    }

    public List<Course> getCourses()
    {
        return courses;
    }

    public void setCourses(List<Course> courses)
    {
        this.courses = courses;
    }

    public List<Tag> getTags()
    {
        return tags;
    }

    public void setTags(List<Tag> tags)
    {
        this.tags = tags;
    }
}
