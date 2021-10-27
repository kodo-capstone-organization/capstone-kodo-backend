package com.spring.kodo.entity.rest.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionWithParticularsResp
{
    String getTutorName();

    String getCustomerName();

    String getCourseName();

    LocalDateTime getDateTimeOfTransaction();

    BigDecimal getPlatformFee();

    String getDisplayPictureUrl();
}
