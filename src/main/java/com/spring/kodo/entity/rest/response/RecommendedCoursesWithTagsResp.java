package com.spring.kodo.entity.rest.response;

import com.spring.kodo.entity.Tag;

import java.util.List;

public class RecommendedCoursesWithTagsResp
{
    private List<CourseResp> courses;
    private List<Tag> tags;

    public RecommendedCoursesWithTagsResp()
    {
    }

    public RecommendedCoursesWithTagsResp(List<CourseResp> courses, List<Tag> tags)
    {
        this.courses = courses;
        this.tags = tags;
    }

    public List<CourseResp> getCourses()
    {
        return courses;
    }

    public void setCourses(List<CourseResp> courses)
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
