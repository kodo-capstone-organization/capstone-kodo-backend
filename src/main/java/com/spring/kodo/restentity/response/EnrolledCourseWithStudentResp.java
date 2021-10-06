package com.spring.kodo.restentity.response;

public interface EnrolledCourseWithStudentResp
{
    Long getAccountId();

    String getUsername();

    String getName();

    String getBio();

    String getEmail();

    String getDisplayPictureUrl();

    Boolean getIsAdmin();

    Boolean getIsActive();

    String getStripeAccountId();

    Long getEnrolledCourseId();

    Double getCompletionPercentage();
}
