package com.spring.kodo.restentity.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TutorCourseEarningsResp
{
      private BigDecimal lifetimeTotalEarnings;
      private List<Map<String, String>> lifetimeEarningsByCourse; // [{'courseId': '5', 'courseName': 'Some Course', 'earnings': '18.50'}, {...}, ...]
      private BigDecimal currentMonthTotalEarnings;
      private List<Map<String, String>> currentMonthEarningsByCourse;

      public TutorCourseEarningsResp()
      {
            lifetimeEarningsByCourse = new ArrayList<>();
            currentMonthEarningsByCourse = new ArrayList<>();
      }

      public BigDecimal getLifetimeTotalEarnings() {
            return lifetimeTotalEarnings;
      }

      public void setLifetimeTotalEarnings(BigDecimal lifetimeTotalEarnings) {
            this.lifetimeTotalEarnings = lifetimeTotalEarnings;
      }

      public List<Map<String, String>> getLifetimeEarningsByCourse() {
            return lifetimeEarningsByCourse;
      }

      public void setLifetimeEarningsByCourse(List<Map<String, String>> lifetimeEarningsByCourse) {
            this.lifetimeEarningsByCourse = lifetimeEarningsByCourse;
      }

      public BigDecimal getCurrentMonthTotalEarnings() {
            return currentMonthTotalEarnings;
      }

      public void setCurrentMonthTotalEarnings(BigDecimal currentMonthTotalEarnings) {
            this.currentMonthTotalEarnings = currentMonthTotalEarnings;
      }

      public List<Map<String, String>> getCurrentMonthEarningsByCourse() {
            return currentMonthEarningsByCourse;
      }

      public void setCurrentMonthEarningsByCourse(List<Map<String, String>> currentMonthEarningsByCourse) {
            this.currentMonthEarningsByCourse = currentMonthEarningsByCourse;
      }
}
