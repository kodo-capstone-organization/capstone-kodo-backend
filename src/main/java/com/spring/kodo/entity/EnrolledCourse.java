package com.spring.kodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "enrolled_course")
@Table(name = "enrolled_course")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EnrolledCourse
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrolledCourseId;

    @Column(nullable = true)
    @Min(0)
    @Max(5)
    private Integer courseRating;

    @Column(nullable = true)
    private LocalDateTime dateTimeOfCompletion;

    @ManyToOne(optional = false)
    private Course parentCourse;

    @OneToMany(targetEntity = EnrolledLesson.class, fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "enrolled_course_id", referencedColumnName = "enrolledCourseId"),
            inverseJoinColumns = @JoinColumn(name = "enrolled_lesson_id", referencedColumnName = "enrolledLessonId"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"enrolled_course_id", "enrolled_lesson_id"})
    )
    private List<EnrolledLesson> enrolledLessons;

    public EnrolledCourse()
    {
        this.courseRating = null;
        this.enrolledLessons = new ArrayList<>();
    }

    public EnrolledCourse(Integer courseRating)
    {
        this();

        this.courseRating = courseRating;
    }

    public EnrolledCourse(Long enrolledCourseId, Integer courseRating)
    {
        this(courseRating);

        this.enrolledCourseId = enrolledCourseId;
    }

    public Long getEnrolledCourseId()
    {
        return enrolledCourseId;
    }

    public void setEnrolledCourseId(Long enrolledCourseId)
    {
        this.enrolledCourseId = enrolledCourseId;
    }

    public Integer getCourseRating()
    {
        return courseRating;
    }

    public void setCourseRating(Integer courseRating)
    {
        this.courseRating = courseRating;
    }

    public LocalDateTime getDateTimeOfCompletion()
    {
        return dateTimeOfCompletion;
    }

    public void setDateTimeOfCompletion(LocalDateTime dateTimeOfCompletion)
    {
        this.dateTimeOfCompletion = dateTimeOfCompletion;
    }

    public Course getParentCourse()
    {
        return parentCourse;
    }

    public void setParentCourse(Course parentCourse)
    {
        this.parentCourse = parentCourse;
    }

    public List<EnrolledLesson> getEnrolledLessons()
    {
        return enrolledLessons;
    }

    public void setEnrolledLessons(List<EnrolledLesson> enrolledLessons)
    {
        this.enrolledLessons = enrolledLessons;
    }

    @Override
    public String toString()
    {
        return "EnrolledCourse{" +
                "enrolledCourseId=" + enrolledCourseId +
                ", courseRating=" + courseRating +
                ", dateTimeOfCompletion=" + dateTimeOfCompletion +
                ", parentCourse=" + parentCourse +
                ", enrolledLessons=" + enrolledLessons +
                '}';
    }
}
