package com.spring.kodo.restentity.response;

public interface EnrolledCourseWithStudentResp
{
    Long getStudentId();

    Long getEnrolledCourseId();

    String getStudentName();

    Double getCompletionPercentage();
}
