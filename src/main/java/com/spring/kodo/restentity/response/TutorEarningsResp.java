package com.spring.kodo.restentity.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class TutorEarningsResp
{
    private Long tutorId;

    private String tutorName;

    private BigDecimal lifetimeTutorEarning;

    private BigDecimal currentMonthTutorEarning;

    private List<MonthlyEarningResp> monthlyTutorEarningsForLastYear;

    private BigDecimal earningsLastMonth;

    private Integer numCoursesTaught;

    private Integer numCoursesCreatedCurrentMonth;

    private Integer numCoursesCreatedLastMonth;

    public TutorEarningsResp() {
    }

    public TutorEarningsResp(Long tutorId, String tutorName, BigDecimal lifetimeTutorEarning, BigDecimal currentMonthTutorEarning, List<MonthlyEarningResp> monthlyTutorEarningsForLastYear) {
        this.tutorId = tutorId;
        this.tutorName = tutorName;
        this.lifetimeTutorEarning = lifetimeTutorEarning;
        this.currentMonthTutorEarning = currentMonthTutorEarning;
        this.monthlyTutorEarningsForLastYear = monthlyTutorEarningsForLastYear;
    }

    public BigDecimal getLifetimeTutorEarning() {
        return lifetimeTutorEarning;
    }

    public void setLifetimeTutorEarning(BigDecimal lifetimeTutorEarning) {
        this.lifetimeTutorEarning = lifetimeTutorEarning;
    }

    public BigDecimal getCurrentMonthTutorEarning() {
        return currentMonthTutorEarning;
    }

    public void setCurrentMonthTutorEarning(BigDecimal currentMonthTutorEarning) {
        this.currentMonthTutorEarning = currentMonthTutorEarning;
    }

    public Long getTutorId() {
        return tutorId;
    }

    public void setTutorId(Long tutorId) {
        this.tutorId = tutorId;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public List<MonthlyEarningResp> getMonthlyTutorEarningsForLastYear() {
        return monthlyTutorEarningsForLastYear;
    }

    public void setMonthlyTutorEarningsForLastYear(List<MonthlyEarningResp> monthlyTutorEarningsForLastYear) {
        this.monthlyTutorEarningsForLastYear = monthlyTutorEarningsForLastYear;
    }

    @Override
    public String toString() {
        return "TutorEarningsResp{" +
                "tutorId=" + tutorId +
                ", tutorName='" + tutorName + '\'' +
                ", lifetimeTutorEarning=" + lifetimeTutorEarning +
                ", currentMonthTutorEarning=" + currentMonthTutorEarning +
                ", monthlyTutorEarningsForLastYear=" + monthlyTutorEarningsForLastYear +
                '}';
    }

    public BigDecimal getEarningsLastMonth() {
        return earningsLastMonth;
    }

    public void setEarningsLastMonth(BigDecimal earningsLastMonth) {
        this.earningsLastMonth = earningsLastMonth;
    }

    public Integer getNumCoursesTaught() {
        return numCoursesTaught;
    }

    public void setNumCoursesTaught(Integer numCoursesTaught) {
        this.numCoursesTaught = numCoursesTaught;
    }

    public Integer getNumCoursesCreatedCurrentMonth() {
        return numCoursesCreatedCurrentMonth;
    }

    public void setNumCoursesCreatedCurrentMonth(Integer numCoursesCreatedCurrentMonth) {
        this.numCoursesCreatedCurrentMonth = numCoursesCreatedCurrentMonth;
    }

    public Integer getNumCoursesCreatedLastMonth() {
        return numCoursesCreatedLastMonth;
    }

    public void setNumCoursesCreatedLastMonth(Integer numCoursesCreatedLastMonth) {
        this.numCoursesCreatedLastMonth = numCoursesCreatedLastMonth;
    }
}
