package com.spring.kodo.entity.rest.response;

import java.math.BigDecimal;
import java.util.List;

public class TagEarningsResp
{
    private Long tagId;

    private String tagName;

    private BigDecimal lifetimeTagEarning;

    private BigDecimal currentMonthTagEarning;

    private BigDecimal lastMonthTagEarning;

    private List<MonthlyEarningResp> monthlyTagEarningForLastYear;

    public TagEarningsResp() {
    }

    public TagEarningsResp(Long tagId, String tagName, BigDecimal lifetimeTagEarning, BigDecimal currentMonthTagEarning, List<MonthlyEarningResp> monthlyTagEarningForLastYear) {
        this.tagId = tagId;
        this.tagName = tagName;
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

    public List<MonthlyEarningResp> getMonthlyTagEarningForLastYear() {
        return monthlyTagEarningForLastYear;
    }

    public void setMonthlyTagEarningForLastYear(List<MonthlyEarningResp> monthlyTagEarningForLastYear) {
        this.monthlyTagEarningForLastYear = monthlyTagEarningForLastYear;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public String toString() {
        return "TagEarningsResp{" +
                "tagId=" + tagId +
                ", tagName='" + tagName + '\'' +
                ", lifetimeTagEarning=" + lifetimeTagEarning +
                ", currentMonthTagEarning=" + currentMonthTagEarning +
                ", monthlyTagEarningForLastYear=" + monthlyTagEarningForLastYear +
                '}';
    }

    public BigDecimal getLastMonthTagEarning() {
        return lastMonthTagEarning;
    }

    public void setLastMonthTagEarning(BigDecimal lastMonthTagEarning) {
        this.lastMonthTagEarning = lastMonthTagEarning;
    }
}
