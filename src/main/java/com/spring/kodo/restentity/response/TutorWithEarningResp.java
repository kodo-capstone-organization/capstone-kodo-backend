package com.spring.kodo.restentity.response;

import java.math.BigDecimal;

public interface TutorWithEarningResp
{
    Long getAccountId();

    String getName();

    BigDecimal getEarnings();
}
