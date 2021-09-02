package com.spring.kodo.restentity.response;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.EnrolledCourse;
import com.spring.kodo.entity.Tag;

import java.math.BigDecimal;
import java.util.List;

public class CourseWithTutorResp
{
    private Long courseId;
    private String name;
    private String description;
    private BigDecimal price;
    private String bannerUrl;
    private List<EnrolledCourse> enrollment;
    private List<Tag> courseTags;
    private Account tutor;

    public CourseWithTutorResp()
    {
    }

    public CourseWithTutorResp(Long courseId, String name, String description, BigDecimal price, String bannerUrl, List<EnrolledCourse> enrollment, List<Tag> courseTags, Account tutor)
    {
        this.courseId = courseId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.bannerUrl = bannerUrl;
        this.enrollment = enrollment;
        this.courseTags = courseTags;
        this.tutor = tutor;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
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

    public Account getTutor()
    {
        return tutor;
    }

    public void setTutor(Account tutor)
    {
        this.tutor = tutor;
    }
}
