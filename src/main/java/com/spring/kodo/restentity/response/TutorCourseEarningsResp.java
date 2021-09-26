package com.spring.kodo.restentity.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TutorCourseEarningsResp
{
      // Summary stats
      private Integer totalEnrollmentCount;
      private Integer totalPublishedCourseCount;
      private Integer totalCourseCount;
      private BigDecimal lifetimeTotalEarnings;
      private List<Map<String, String>> lifetimeEarningsByCourse; // [{'courseId': '5', 'courseName': 'Some Course', 'earnings': '18.50', 'courseNameWithEarnings': 'Some Course ($xx)'}, {...}, ...]
      private BigDecimal currentMonthTotalEarnings;
      private List<Map<String, String>> currentMonthEarningsByCourse;

      // Course stats
      private List<Map<String, String>> courseStatsByMonthForLastYear;

      public TutorCourseEarningsResp()
      {
            lifetimeEarningsByCourse = new ArrayList<>();
            currentMonthEarningsByCourse = new ArrayList<>();
            courseStatsByMonthForLastYear = new ArrayList<>();
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

      public List<Map<String, String>> getCourseStatsByMonthForLastYear() {
            return courseStatsByMonthForLastYear;
      }

      public void setCourseStatsByMonthForLastYear(List<Map<String, String>> courseStatsByMonthForLastYear) {
            this.courseStatsByMonthForLastYear = courseStatsByMonthForLastYear;
      }

      public Integer getTotalEnrollmentCount() {
            return totalEnrollmentCount;
      }

      public void setTotalEnrollmentCount(Integer totalEnrollmentCount) {
            this.totalEnrollmentCount = totalEnrollmentCount;
      }

      public Integer getTotalPublishedCourseCount() {
            return totalPublishedCourseCount;
      }

      public void setTotalPublishedCourseCount(Integer totalPublishedCourseCount) {
            this.totalPublishedCourseCount = totalPublishedCourseCount;
      }

      public Integer getTotalCourseCount() {
            return totalCourseCount;
      }

      public void setTotalCourseCount(Integer totalCourseCount) {
            this.totalCourseCount = totalCourseCount;
      }
}
