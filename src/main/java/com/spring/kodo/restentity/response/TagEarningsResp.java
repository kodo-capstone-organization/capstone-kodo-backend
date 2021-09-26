package com.spring.kodo.restentity.response;

import java.math.BigDecimal;
import java.util.Map;

public class TagEarningsResp
{
    private BigDecimal lifetimeTagEarning;

    private BigDecimal currentMonthTagEarning;

    private Map<String, BigDecimal> monthlyTagEarningForLastYear;

    public TagEarningsResp() {
    }

    public TagEarningsResp(BigDecimal lifetimeTagEarning, BigDecimal currentMonthTagEarning, Map<String, BigDecimal> monthlyTagEarningForLastYear) {
        this.lifetimeTagEarning = lifetimeTagEarning;
        this.currentMonthTagEarning = currentMonthTagEarning;
        this.monthlyTagEarningForLastYear = monthlyTagEarningForLastYear;
    }

    public BigDecimal getLifetimeTagEarning() {
        return lifetimeTagEarning;
    }

    public void setLifetimeTagEarning(BigDecimal lifetimeTagEarning) {
        this.lifetimeTagEarning = lifetimeTagEarning;
    }

    public BigDecimal getCurrentMonthTagEarning() {
        return currentMonthTagEarning;
    }

    public void setCurrentMonthTagEarning(BigDecimal currentMonthTagEarning) {
        this.currentMonthTagEarning = currentMonthTagEarning;
    }

    public Map<String, BigDecimal> getMonthlyTagEarningForLastYear() {
        return monthlyTagEarningForLastYear;
    }

    public void setMonthlyTagEarningForLastYear(Map<String, BigDecimal> monthlyTagEarningForLastYear) {
        this.monthlyTagEarningForLastYear = monthlyTagEarningForLastYear;
    }
}
