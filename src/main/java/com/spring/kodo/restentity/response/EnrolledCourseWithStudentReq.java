package com.spring.kodo.restentity.response;

import java.math.BigDecimal;

public class EnrolledCourseWithStudentReq {
    private String courseName;
    private String studentName;
    private BigDecimal completionPercentage;

    public EnrolledCourseWithStudentReq() {
    }

    public EnrolledCourseWithStudentReq(String courseName, String studentName, BigDecimal completionPercentage) {
        this.courseName = courseName;
        this.studentName = studentName;
        this.completionPercentage = completionPercentage;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public BigDecimal getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(BigDecimal completionPercentage) {
        this.completionPercentage = completionPercentage;
    }
}
