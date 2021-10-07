package com.spring.kodo.restentity.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionWithParticularsResp
{
    String getTutorName();

    String getCustomerName();

    String getCourseName();

    LocalDateTime getDateTimeOfTransaction();

    BigDecimal getPlatformFee();
}
