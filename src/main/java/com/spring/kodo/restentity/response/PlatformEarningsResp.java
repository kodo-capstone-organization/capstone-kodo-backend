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

    private List<CourseWithEarningResp> lifetimeHighestEarningCourses;

    private List<CourseWithEarningResp> currentMonthHighestEarningCourses;

    public PlatformEarningsResp() {
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

    public Boolean getIncreaseInMonthlyProfit() {
        return this.currentMonthPlatformEarnings.compareTo(lastMonthPlatformEarnings) == 1 ? true : false;
    }

    public List<CourseWithEarningResp> getLifetimeHighestEarningCourses() {
        return lifetimeHighestEarningCourses;
    }

    public void setLifetimeHighestEarningCourses(List<CourseWithEarningResp> lifetimeHighestEarningCourses) {
        this.lifetimeHighestEarningCourses = lifetimeHighestEarningCourses;
    }

    public List<CourseWithEarningResp> getCurrentMonthHighestEarningCourses() {
        return currentMonthHighestEarningCourses;
    }

    public void setCurrentMonthHighestEarningCourses(List<CourseWithEarningResp> currentMonthHighestEarningCourses) {
        this.currentMonthHighestEarningCourses = currentMonthHighestEarningCourses;
    }
}
