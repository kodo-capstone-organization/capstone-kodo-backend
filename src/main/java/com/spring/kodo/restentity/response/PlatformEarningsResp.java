package com.spring.kodo.restentity.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class PlatformEarningsResp
{
    private BigDecimal lifetimePlatformEarnings;

    private BigDecimal currentMonthPlatformEarnings;

    private BigDecimal lastMonthPlatformEarnings;

    private List<MonthlyEarningResp> monthlyPlatformEarningsForLastYear;

    public PlatformEarningsResp() {
    }

    public PlatformEarningsResp(BigDecimal lifetimePlatformEarnings, BigDecimal currentMonthPlatformEarnings, BigDecimal lastMonthPlatformEarnings, List<MonthlyEarningResp> monthlyPlatformEarningsForLastYear) {
        this.lifetimePlatformEarnings = lifetimePlatformEarnings;
        this.currentMonthPlatformEarnings = currentMonthPlatformEarnings;
        this.lastMonthPlatformEarnings = lastMonthPlatformEarnings;
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

    public List<MonthlyEarningResp> getMonthlyPlatformEarningsForLastYear() {
        return monthlyPlatformEarningsForLastYear;
    }

    public void setMonthlyPlatformEarningsForLastYear(List<MonthlyEarningResp> monthlyPlatformEarningsForLastYear) {
        this.monthlyPlatformEarningsForLastYear = monthlyPlatformEarningsForLastYear;
    }

    public BigDecimal getLastMonthPlatformEarnings() {
        return lastMonthPlatformEarnings;
    }

    public void setLastMonthPlatformEarnings(BigDecimal lastMonthPlatformEarnings) {
        this.lastMonthPlatformEarnings = lastMonthPlatformEarnings;
    }

    @Override
    public String toString() {
        return "PlatformEarningsResp{" +
                "lifetimePlatformEarnings=" + lifetimePlatformEarnings +
                ", currentMonthPlatformEarnings=" + currentMonthPlatformEarnings +
                ", lastMonthPlatformEarnings=" + lastMonthPlatformEarnings +
                ", monthlyPlatformEarningsForLastYear=" + monthlyPlatformEarningsForLastYear +
                '}';
    }
}
