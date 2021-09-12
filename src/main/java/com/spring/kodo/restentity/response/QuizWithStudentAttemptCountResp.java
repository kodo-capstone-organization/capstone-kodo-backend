package com.spring.kodo.restentity.response;

import java.time.LocalTime;

public interface QuizWithStudentAttemptCountResp
{
    Long getContentId();

    LocalTime getTimeLimit();

    Integer getMaxAttemptsPerStudent();

    Integer getStudentAttemptCount();
}
