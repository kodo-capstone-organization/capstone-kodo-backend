package com.spring.kodo.restentity.response;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Tag;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CourseResp
{
    private Long courseId;
    private String name;
    private String description;
    private BigDecimal price;
    private String bannerUrl;
    private LocalDateTime dateTimeOfCreation;
    private List<Tag> courseTags;
    private Account tutor;
    private String bannerPictureFileName;
    private Boolean isEnrollmentActive;
    private Boolean isReviewRequested;
    private Double courseRating;
    private Integer enrollmentLength;

    public CourseResp(Long courseId, String name, String description, BigDecimal price, String bannerUrl, LocalDateTime dateTimeOfCreation, List<Tag> courseTags, Account tutor, String bannerPictureFileName, Boolean isEnrollmentActive, Double courseRating, Integer enrollmentLength, Boolean isReviewRequested) {
        this.courseId = courseId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.bannerUrl = bannerUrl;
        this.dateTimeOfCreation = dateTimeOfCreation;
        this.courseTags = courseTags;
        this.tutor = tutor;
        this.bannerPictureFileName = bannerPictureFileName;
        this.isEnrollmentActive = isEnrollmentActive;
        this.courseRating = courseRating;
        this.enrollmentLength = enrollmentLength;
        this.isReviewRequested = isReviewRequested;
    }

    public Boolean getEnrollmentActive() {
        return isEnrollmentActive;
    }

    public void setEnrollmentActive(Boolean enrollmentActive) {
        isEnrollmentActive = enrollmentActive;
    }

    public Integer getEnrollmentLength() {
        return enrollmentLength;
    }

    public void setEnrollmentLength(Integer enrollmentLength) {
        this.enrollmentLength = enrollmentLength;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public LocalDateTime getDateTimeOfCreation() {
        return dateTimeOfCreation;
    }

    public void setDateTimeOfCreation(LocalDateTime dateTimeOfCreation) {
        this.dateTimeOfCreation = dateTimeOfCreation;
    }

    public List<Tag> getCourseTags() {
        return courseTags;
    }

    public void setCourseTags(List<Tag> courseTags) {
        this.courseTags = courseTags;
    }

    public Account getTutor() {
        return tutor;
    }

    public void setTutor(Account tutor) {
        this.tutor = tutor;
    }

    public String getBannerPictureFileName() {
        return bannerPictureFileName;
    }

    public void setBannerPictureFileName(String bannerPictureFileName) {
        this.bannerPictureFileName = bannerPictureFileName;
    }

    public Boolean getIsEnrollmentActive() {
        return isEnrollmentActive;
    }

    public void setIsEnrollmentActive(Boolean enrollmentActive) {
        isEnrollmentActive = enrollmentActive;
    }

    public Double getCourseRating() {
        return courseRating;
    }

    public void setCourseRating(Double courseRating) {
        this.courseRating = courseRating;
    }

    public Boolean getIsReviewRequested() {
        return isReviewRequested;
    }

    public void setIsReviewRequested(Boolean reviewRequested) {
        isReviewRequested = reviewRequested;
    }

    @Override
    public String toString() {
        return "CourseResp{" +
                "courseId=" + courseId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", bannerUrl='" + bannerUrl + '\'' +
                ", dateTimeOfCreation=" + dateTimeOfCreation +
                ", courseTags=" + courseTags +
                ", tutor=" + tutor +
                ", bannerPictureFileName='" + bannerPictureFileName + '\'' +
                ", isEnrollmentActive=" + isEnrollmentActive +
                ", isReviewRequested=" + isReviewRequested +
                ", courseRating=" + courseRating +
                ", enrollmentLength=" + enrollmentLength +
                '}';
    }
}
