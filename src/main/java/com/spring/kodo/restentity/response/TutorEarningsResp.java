package com.spring.kodo.restentity.response;

import java.math.BigDecimal;
import java.util.Map;

public class TutorEarningsResp
{
    private BigDecimal lifetimeTutorEarning;

    private BigDecimal currentMonthTutorEarning;

    private Map<String, BigDecimal> monthlyTutorEarningsForLastYear;

    public TutorEarningsResp() {
    }

    public TutorEarningsResp(BigDecimal lifetimeTutorEarning, BigDecimal currentMonthTutorEarning, Map<String, BigDecimal> monthlyTutorEarningsForLastYear) {
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

    public Map<String, BigDecimal> getMonthlyTutorEarningsForLastYear() {
        return monthlyTutorEarningsForLastYear;
    }

    public void setMonthlyTutorEarningsForLastYear(Map<String, BigDecimal> monthlyTutorEarningsForLastYear) {
        this.monthlyTutorEarningsForLastYear = monthlyTutorEarningsForLastYear;
    }
}
