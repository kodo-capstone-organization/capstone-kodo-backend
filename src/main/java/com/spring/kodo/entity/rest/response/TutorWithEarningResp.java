package com.spring.kodo.entity.rest.response;

import java.math.BigDecimal;

public interface TutorWithEarningResp
{
    Long getAccountId();

    String getName();

    BigDecimal getEarnings();
}
