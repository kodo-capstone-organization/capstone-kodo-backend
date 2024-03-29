package com.spring.kodo.entity.rest.request;

import java.math.BigDecimal;

public class StripePaymentReq
{
    private Long studentId;

    private Long tutorId;
    private String tutorName;
    private Long courseId;

    private BigDecimal amount;
    private String tutorStripeAccountId;

    public StripePaymentReq(){}

    public StripePaymentReq(Long studentId, Long tutorId, Long courseId, String tutorName, BigDecimal amount, String tutorStripeAccountId) {
        this.studentId = studentId;
        this.tutorId = tutorId;
        this.courseId = courseId;
        this.tutorName = tutorName;
        this.amount = amount;
        this.tutorStripeAccountId = tutorStripeAccountId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTutorStripeAccountId() {
        return tutorStripeAccountId;
    }

    public void setTutorStripeAccountId(String tutorStripeAccountId) {
        this.tutorStripeAccountId = tutorStripeAccountId;
    }
}
