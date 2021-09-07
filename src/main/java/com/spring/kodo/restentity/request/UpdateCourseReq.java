package com.spring.kodo.restentity.request;

import com.spring.kodo.entity.Course;

import java.util.List;

public class UpdateCourseReq
{
    private Course course;
    private List<String> courseTagTitles;
    private List<UpdateLessonReq> updateLessonReqs; // Special case, as we want to handle lesson update during course update
    private List<Long> enrolledCourseIds;

    public UpdateCourseReq(Course course, List<String> courseTagTitles, List<UpdateLessonReq> updateLessonReqs, List<Long> enrolledCourseIds) {
        this.course = course;
        this.courseTagTitles = courseTagTitles;
        this.updateLessonReqs = updateLessonReqs;
        this.enrolledCourseIds = enrolledCourseIds;
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

    public List<UpdateLessonReq> getUpdateLessonReqs() {
        return updateLessonReqs;
    }

    public void setUpdateLessonReqs(List<UpdateLessonReq> updateLessonReqs) {
        this.updateLessonReqs = updateLessonReqs;
    }

    public List<Long> getEnrolledCourseIds() {
        return enrolledCourseIds;
    }

    public void setEnrolledCourseIds(List<Long> enrolledCourseIds) {
        this.enrolledCourseIds = enrolledCourseIds;
    }
}
