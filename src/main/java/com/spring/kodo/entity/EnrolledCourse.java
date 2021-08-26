package com.spring.kodo.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class EnrolledCourse
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrolledCourseId;

    @Size(min = 0, max = 5)
    private int courseRating;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Course parentCourse;

    @OneToMany(targetEntity = CompletedLesson.class, fetch = FetchType.LAZY)
    private List<CompletedLesson> completedLessons;

    public EnrolledCourse()
    {
        this.completedLessons = new ArrayList<>();
    }

    public EnrolledCourse(int courseRating)
    {
        this();

        this.courseRating = courseRating;
    }

    public Long getEnrolledCourseId()
    {
        return enrolledCourseId;
    }

    public void setEnrolledCourseId(Long enrolledCourseId)
    {
        this.enrolledCourseId = enrolledCourseId;
    }

    public int getCourseRating()
    {
        return courseRating;
    }

    public void setCourseRating(int courseRating)
    {
        this.courseRating = courseRating;
    }

    public Course getParentCourse()
    {
        return parentCourse;
    }

    public void setParentCourse(Course parentCourse)
    {
        this.parentCourse = parentCourse;
    }

    public List<CompletedLesson> getCompletedLessons()
    {
        return completedLessons;
    }

    public void setCompletedLessons(List<CompletedLesson> completedLessons)
    {
        this.completedLessons = completedLessons;
    }

    @Override
    public String toString()
    {
        return "EnrolledCourse{" +
                "enrolledCourseId=" + enrolledCourseId +
                ", courseRating=" + courseRating +
                ", parentCourse=" + parentCourse +
                ", completedLessons=" + completedLessons +
                '}';
    }
}
