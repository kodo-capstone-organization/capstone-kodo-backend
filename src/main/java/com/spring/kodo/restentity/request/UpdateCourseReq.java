package com.spring.kodo.restentity.request;

import com.spring.kodo.entity.Course;

import java.util.List;

public class UpdateCourseReq
{
    private Course course;
    private List<String> courseTagTitles;

    public UpdateCourseReq(Course course, List<String> courseTagTitles) {
        this.course = course;
        this.courseTagTitles = courseTagTitles;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<String> getCourseTagTitles() {
        return courseTagTitles;
    }

    public void setCourseTagTitles(List<String> courseTagTitles) {
        this.courseTagTitles = courseTagTitles;
    }
}
