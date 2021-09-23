package com.spring.kodo.restentity.response;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface EnrolledCourseWithStudentCompletion {

    Long getStudentId();

    String getStudentName();

    String getStudentUsername();

    Boolean getStudentActive();

    Timestamp getCompletionDate();
}
