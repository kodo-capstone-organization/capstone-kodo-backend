package com.spring.kodo.util;

import java.math.BigDecimal;

// Declare any program-wide constants here.
// To use in other files, import Constants and do Constants.field
public class Constants
{
    public static final String PROJECT_TITLE = "Kodo";
    public static final String PROJECT_NAME = "kodo-backend";

    public static final BigDecimal STRIPE_FEE_ACCOUNT_BASE = BigDecimal.valueOf(0.50);
    public static final BigDecimal STRIPE_FEE_PERCENTAGE = BigDecimal.valueOf(0.34);
    public static final BigDecimal PLATFORM_FEE_PERCENTAGE = BigDecimal.valueOf(0.35);
}
