package com.spring.kodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StudentAttemptAnswer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentAttemptAnswerId;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime dateTimeOfAttempt;

    @Column(nullable = false, length = 512)
    @NotBlank(message = "Left Content cannot be blank")
    @Size(min = 0, max = 512)
    private String leftContent;

    @Column(length = 512)
    @Size(min = 0, max = 512)
    private String rightContent;

    @Column
    private Boolean correct;

    @Column(precision = 11, scale = 2)
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2)
    private Double marks;

    public StudentAttemptAnswer()
    {
        this.dateTimeOfAttempt = LocalDateTime.now();
    }

    public StudentAttemptAnswer(String leftContent)
    {
        this();

        this.leftContent = leftContent;
    }

    public StudentAttemptAnswer(String leftContent, String rightContent)
    {
        this(leftContent);

        this.rightContent = rightContent;
    }

    public Long getStudentAttemptAnswerId()
    {
        return studentAttemptAnswerId;
    }

    public void setStudentAttemptAnswerId(Long studentAttemptAnswerId)
    {
        this.studentAttemptAnswerId = studentAttemptAnswerId;
    }

    public LocalDateTime getDateTimeOfAttempt()
    {
        return dateTimeOfAttempt;
    }

    public void setDateTimeOfAttempt(LocalDateTime dateTimeOfAttempt)
    {
        this.dateTimeOfAttempt = dateTimeOfAttempt;
    }

    public String getLeftContent()
    {
        return leftContent;
    }

    public void setLeftContent(String leftContent)
    {
        this.leftContent = leftContent;
    }

    public String getRightContent()
    {
        return rightContent;
    }

    public void setRightContent(String rightContent)
    {
        this.rightContent = rightContent;
    }

    public Boolean getCorrect()
    {
        return correct;
    }

    public void setCorrect(Boolean correct)
    {
        this.correct = correct;
    }

    public Double getMarks()
    {
        return marks;
    }

    public void setMarks(Double marks)
    {
        this.marks = marks;
    }

    @Override
    public String toString()
    {
        return "StudentAttemptAnswer{" +
                "studentAttemptAnswerId=" + studentAttemptAnswerId +
                ", dateTimeOfAttempt=" + dateTimeOfAttempt +
                ", leftContent='" + leftContent + '\'' +
                ", rightContent='" + rightContent + '\'' +
                ", correct=" + correct +
                ", marks=" + marks +
                '}';
    }
}
