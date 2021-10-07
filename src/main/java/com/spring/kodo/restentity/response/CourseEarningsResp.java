package com.spring.kodo.restentity.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class CourseEarningsResp
{
    private Long courseId;

    private String courseName;

    private String tutorName;

    private Integer numberOfEnrollment;

    private BigDecimal lifetimeCourseEarning;

    private BigDecimal currentMonthCourseEarning;
    //new
    private BigDecimal lastMonthCourseEarning;
    //new
    private BigDecimal numEnrollmentMonth;
    //new
    private BigDecimal numEnrollmentLastMonth;
    //new
    private BigDecimal percentageCompletion;

    private List<MonthlyEarningResp> monthlyCourseEarningForLastYear;

    public CourseEarningsResp() {
    }

    public CourseEarningsResp(Long courseId, String courseName, String tutorName, Integer numberOfEnrollment, BigDecimal lifetimeCourseEarning, BigDecimal currentMonthCourseEarning, List<MonthlyEarningResp> monthlyCourseEarningForLastYear) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.tutorName = tutorName;
        this.numberOfEnrollment = numberOfEnrollment;
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

    public List<MonthlyEarningResp> getMonthlyCourseEarningForLastYear() {
        return monthlyCourseEarningForLastYear;
    }

    public void setMonthlyCourseEarningForLastYear(List<MonthlyEarningResp> monthlyCourseEarningForLastYear) {
        this.monthlyCourseEarningForLastYear = monthlyCourseEarningForLastYear;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public Integer getNumberOfEnrollment() {
        return numberOfEnrollment;
    }

    public void setNumberOfEnrollment(Integer numberOfEnrollment) {
        this.numberOfEnrollment = numberOfEnrollment;
    }

    @Override
    public String toString() {
        return "CourseEarningsResp{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", tutorName='" + tutorName + '\'' +
                ", numberOfEnrollment=" + numberOfEnrollment +
                ", lifetimeCourseEarning=" + lifetimeCourseEarning +
                ", currentMonthCourseEarning=" + currentMonthCourseEarning +
                ", monthlyCourseEarningForLastYear=" + monthlyCourseEarningForLastYear +
                '}';
    }



    public BigDecimal getNumEnrollmentMonth() {
        return numEnrollmentMonth;
    }

    public void setNumEnrollmentMonth(BigDecimal numEnrollmentMonth) {
        this.numEnrollmentMonth = numEnrollmentMonth;
    }



    public BigDecimal getPercentageCompletion() {
        return percentageCompletion;
    }

    public void setPercentageCompletion(BigDecimal percentageCompletion) {
        this.percentageCompletion = percentageCompletion;
    }

    public BigDecimal getLastMonthCourseEarning() {
        return lastMonthCourseEarning;
    }

    public void setLastMonthCourseEarning(BigDecimal lastMonthCourseEarning) {
        this.lastMonthCourseEarning = lastMonthCourseEarning;
    }

    public BigDecimal getNumEnrollmentLastMonth() {
        return numEnrollmentLastMonth;
    }

    public void setNumEnrollmentLastMonth(BigDecimal numEnrollmentLastMonth) {
        this.numEnrollmentLastMonth = numEnrollmentLastMonth;
    }
}
