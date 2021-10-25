package com.spring.kodo.entity.rest.response;

import java.math.BigDecimal;

public interface CourseWithEarningResp
{
    Long getCourseId();

    String getName();

    BigDecimal getEarnings();
}
