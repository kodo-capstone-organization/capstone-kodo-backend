package com.spring.kodo.restentity.response;

import java.math.BigDecimal;
import java.util.Map;

public class PlatformEarningsResp
{
    private BigDecimal lifetimePlatformEarnings;

    private BigDecimal currentMonthPlatformEarnings;

    private BigDecimal lastMonthPlatformEarnings;

    private Map<String, BigDecimal> monthlyPlatformEarningsForLastYear;

    public PlatformEarningsResp() {
    }

    public PlatformEarningsResp(BigDecimal lifetimePlatformEarnings, BigDecimal currentMonthPlatformEarnings, Map<String, BigDecimal> monthlyPlatformEarningsForLastYear) {
        this.lifetimePlatformEarnings = lifetimePlatformEarnings;
        this.currentMonthPlatformEarnings = currentMonthPlatformEarnings;
        this.monthlyPlatformEarningsForLastYear = monthlyPlatformEarningsForLastYear;
    }

    public BigDecimal getLifetimePlatformEarnings() {
        return lifetimePlatformEarnings;
    }

    public void setLifetimePlatformEarnings(BigDecimal lifetimePlatformEarnings) {
        this.lifetimePlatformEarnings = lifetimePlatformEarnings;
    }

    public BigDecimal getCurrentMonthPlatformEarnings() {
        return currentMonthPlatformEarnings;
    }

    public void setCurrentMonthPlatformEarnings(BigDecimal currentMonthPlatformEarnings) {
        this.currentMonthPlatformEarnings = currentMonthPlatformEarnings;
    }

    public Map<String, BigDecimal> getMonthlyPlatformEarningsForLastYear() {
        return monthlyPlatformEarningsForLastYear;
    }

    public void setMonthlyPlatformEarningsForLastYear(Map<String, BigDecimal> monthlyPlatformEarningsForLastYear) {
        this.monthlyPlatformEarningsForLastYear = monthlyPlatformEarningsForLastYear;
    }

    public BigDecimal getLastMonthPlatformEarnings() {
        return lastMonthPlatformEarnings;
    }

    public void setLastMonthPlatformEarnings(BigDecimal lastMonthPlatformEarnings) {
        this.lastMonthPlatformEarnings = lastMonthPlatformEarnings;
    }
}
