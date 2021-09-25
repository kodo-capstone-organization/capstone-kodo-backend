package com.spring.kodo.restentity.response;

import java.math.BigDecimal;
import java.util.Map;

public class CourseEarningsResp
{
    private BigDecimal lifetimeCourseEarning;

    private BigDecimal currentMonthCourseEarning;

    private Map<String, BigDecimal> monthlyCourseEarningForLastYear;

    public CourseEarningsResp() {
    }

    public CourseEarningsResp(BigDecimal lifetimeCourseEarning, BigDecimal currentMonthCourseEarning, Map<String, BigDecimal> monthlyCourseEarningForLastYear) {
        this.lifetimeCourseEarning = lifetimeCourseEarning;
        this.currentMonthCourseEarning = currentMonthCourseEarning;
        this.monthlyCourseEarningForLastYear = monthlyCourseEarningForLastYear;
    }

    public BigDecimal getLifetimeCourseEarning() {
        return lifetimeCourseEarning;
    }

    public void setLifetimeCourseEarning(BigDecimal lifetimeCourseEarning) {
        this.lifetimeCourseEarning = lifetimeCourseEarning;
    }

    public BigDecimal getCurrentMonthCourseEarning() {
        return currentMonthCourseEarning;
    }

    public void setCurrentMonthCourseEarning(BigDecimal currentMonthCourseEarning) {
        this.currentMonthCourseEarning = currentMonthCourseEarning;
    }

    public Map<String, BigDecimal> getMonthlyCourseEarningForLastYear() {
        return monthlyCourseEarningForLastYear;
    }

    public void setMonthlyCourseEarningForLastYear(Map<String, BigDecimal> monthlyCourseEarningForLastYear) {
        this.monthlyCourseEarningForLastYear = monthlyCourseEarningForLastYear;
    }
}
