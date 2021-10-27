package com.spring.kodo.entity.rest.response;

import java.math.BigDecimal;

public class MonthlyEarningResp
{
    private String month;

    private BigDecimal earnings;

    public MonthlyEarningResp() {
    }

    public MonthlyEarningResp(String month, BigDecimal earnings) {
        this.month = month;
        this.earnings = earnings;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getEarnings() {
        return earnings;
    }

    public void setEarnings(BigDecimal earnings) {
        this.earnings = earnings;
    }

    @Override
    public String toString() {
        return "MonthlyEarningResp{" +
                "month='" + month + '\'' +
                ", earnings=" + earnings +
                '}';
    }
}
