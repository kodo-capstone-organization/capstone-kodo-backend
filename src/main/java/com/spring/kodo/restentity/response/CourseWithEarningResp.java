package com.spring.kodo.restentity.response;

import com.spring.kodo.entity.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface CourseWithEarningResp
{
    Long getCourseId();

    String getName();

    BigDecimal getEarnings();
}
