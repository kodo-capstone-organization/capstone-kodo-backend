package com.spring.kodo.entity.rest.response;

import java.sql.Timestamp;

public interface EnrolledCourseWithStudentCompletion {

    Long getStudentId();

    String getStudentName();

    String getStudentUsername();

    Boolean getStudentActive();

    Timestamp getCompletionDate();
}
