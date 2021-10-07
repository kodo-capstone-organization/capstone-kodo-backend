package com.spring.kodo.restentity.response;

import java.math.BigDecimal;
import java.util.List;

public class PlatformEarningsResp
{
    private BigDecimal lifetimePlatformEarnings;

    private BigDecimal currentMonthPlatformEarnings;

    private BigDecimal lastMonthPlatformEarnings;

    private List<MonthlyEarningResp> monthlyPlatformEarningsForLastYear;

    private List<CourseWithEarningResp> lifetimeHighestEarningCourses;

    private List<CourseWithEarningResp> currentMonthHighestEarningCourses;

    private List<TutorWithEarningResp> lifetimeHighestEarningTutors;

    private List<TutorWithEarningResp> currentMonthHighestEarningTutors;

    private Integer currentMonthNumberOfAccountCreation;

    private Boolean increaseInMonthlyAccountCreation;

    private Integer currentMonthNumberOfEnrollments;

    private Boolean increaseInMonthlyEnrollment;

    private Integer currentMonthNumberOfCourseCreation;

    private Boolean increaseInMonthlyCourseCreation;

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

    public List<TutorWithEarningResp> getLifetimeHighestEarningTutors() {
        return lifetimeHighestEarningTutors;
    }

    public void setLifetimeHighestEarningTutors(List<TutorWithEarningResp> lifetimeHighestEarningTutors) {
        this.lifetimeHighestEarningTutors = lifetimeHighestEarningTutors;
    }

    public List<TutorWithEarningResp> getCurrentMonthHighestEarningTutors() {
        return currentMonthHighestEarningTutors;
    }

    public void setCurrentMonthHighestEarningTutors(List<TutorWithEarningResp> currentMonthHighestEarningTutors) {
        this.currentMonthHighestEarningTutors = currentMonthHighestEarningTutors;
    }

    public Integer getCurrentMonthNumberOfAccountCreation() {
        return currentMonthNumberOfAccountCreation;
    }

    public void setCurrentMonthNumberOfAccountCreation(Integer currentMonthNumberOfAccountCreation) {
        this.currentMonthNumberOfAccountCreation = currentMonthNumberOfAccountCreation;
    }

    public Boolean getIncreaseInMonthlyAccountCreation() {
        return increaseInMonthlyAccountCreation;
    }

    public void setIncreaseInMonthlyAccountCreation(Boolean increaseInMonthlyAccountCreation) {
        this.increaseInMonthlyAccountCreation = increaseInMonthlyAccountCreation;
    }

    public Integer getCurrentMonthNumberOfEnrollments() {
        return currentMonthNumberOfEnrollments;
    }

    public void setCurrentMonthNumberOfEnrollments(Integer currentMonthNumberOfEnrollments) {
        this.currentMonthNumberOfEnrollments = currentMonthNumberOfEnrollments;
    }

    public Boolean getIncreaseInMonthlyEnrollment() {
        return increaseInMonthlyEnrollment;
    }

    public void setIncreaseInMonthlyEnrollment(Boolean increaseInMonthlyEnrollment) {
        this.increaseInMonthlyEnrollment = increaseInMonthlyEnrollment;
    }

    public Integer getCurrentMonthNumberOfCourseCreation() {
        return currentMonthNumberOfCourseCreation;
    }

    public void setCurrentMonthNumberOfCourseCreation(Integer currentMonthNumberOfCourseCreation) {
        this.currentMonthNumberOfCourseCreation = currentMonthNumberOfCourseCreation;
    }

    public Boolean getIncreaseInMonthlyCourseCreation() {
        return increaseInMonthlyCourseCreation;
    }

    public void setIncreaseInMonthlyCourseCreation(Boolean increaseInMonthlyCourseCreation) {
        this.increaseInMonthlyCourseCreation = increaseInMonthlyCourseCreation;
    }
}
