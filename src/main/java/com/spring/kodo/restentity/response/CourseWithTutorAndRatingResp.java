package com.spring.kodo.restentity.response;

import com.spring.kodo.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CourseWithTutorAndRatingResp
{
    private Long courseId;
    private String name;
    private String description;
    private BigDecimal price;
    private String bannerUrl;
    private LocalDateTime dateTimeOfCreation;
    private List<EnrolledCourse> enrollment;
    private List<Tag> courseTags;
    private List<Lesson> lessons;
    private Account tutor;
    private String bannerPictureFileName;
    private Boolean isEnrollmentActive;
    private Double courseRating;

    public CourseWithTutorAndRatingResp()
    {
    }

    public CourseWithTutorAndRatingResp(Long courseId, String name, String description, BigDecimal price, String bannerUrl, LocalDateTime dateTimeOfCreation, List<EnrolledCourse> enrollment, List<Tag> courseTags, List<Lesson> lessons, Account tutor, String bannerPictureFileName, Boolean isEnrollmentActive, Double courseRating)
    {
        this.courseId = courseId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.bannerUrl = bannerUrl;
        this.dateTimeOfCreation = dateTimeOfCreation;
        this.enrollment = enrollment;
        this.courseTags = courseTags;
        this.lessons = lessons;
        this.tutor = tutor;
        this.bannerPictureFileName = bannerPictureFileName;
        this.isEnrollmentActive = isEnrollmentActive;
        this.courseRating = courseRating;
    }

    public CourseWithTutorAndRatingResp(Course course, Account tutor, Double courseRating)
    {
        this.courseId = course.getCourseId();
        this.name = course.getName();
        this.description = course.getDescription();
        this.price = course.getPrice();
        this.bannerUrl = course.getBannerUrl();
        this.enrollment = course.getEnrollment();
        this.courseTags = course.getCourseTags();
        this.lessons = course.getLessons();
        this.tutor = tutor;
        this.bannerPictureFileName = course.getBannerPictureFilename();
        this.isEnrollmentActive = course.getIsEnrollmentActive();
        this.courseRating = courseRating;
    }

    public Long getCourseId()
    {
        return courseId;
    }

    public void setCourseId(Long courseId)
    {
        this.courseId = courseId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public String getBannerUrl()
    {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl)
    {
        this.bannerUrl = bannerUrl;
    }

    public List<EnrolledCourse> getEnrollment()
    {
        return enrollment;
    }

    public void setEnrollment(List<EnrolledCourse> enrollment)
    {
        this.enrollment = enrollment;
    }

    public List<Tag> getCourseTags()
    {
        return courseTags;
    }

    public void setCourseTags(List<Tag> courseTags)
    {
        this.courseTags = courseTags;
    }

    public List<Lesson> getLessons()
    {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons)
    {
        this.lessons = lessons;
    }

    public Account getTutor()
    {
        return tutor;
    }

    public void setTutor(Account tutor)
    {
        this.tutor = tutor;
    }

    public String getBannerPictureFileName()
    {
        return bannerPictureFileName;
    }

    public void setBannerPictureFileName(String bannerPictureFileName)
    {
        this.bannerPictureFileName = bannerPictureFileName;
    }

    public Boolean getIsEnrollmentActive()
    {
        return isEnrollmentActive;
    }

    public void setIsEnrollmentActive(Boolean enrollmentActive)
    {
        isEnrollmentActive = enrollmentActive;
    }

    public Double getCourseRating()
    {
        return courseRating;
    }

    public void setCourseRating(Double courseRating)
    {
        this.courseRating = courseRating;
    }

    public LocalDateTime getDateTimeOfCreation() {
        return dateTimeOfCreation;
    }

    public void setDateTimeOfCreation(LocalDateTime dateTimeOfCreation) {
        this.dateTimeOfCreation = dateTimeOfCreation;
    }
}