package com.spring.kodo.entity.rest.response;

import java.time.LocalTime;

public interface QuizWithStudentAttemptCountResp
{
    Long getContentId();

    LocalTime getTimeLimit();

    Integer getMaxAttemptsPerStudent();

    Integer getStudentAttemptCount();
}
