package com.spring.kodo.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table
public class StudentAttemptAnswer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentAttemptAnswerId;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime dateTimeOfAttempt;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer marks;

    @ManyToOne(optional = false, targetEntity = QuizQuestionOption.class, fetch = FetchType.LAZY)
    private QuizQuestionOption quizQuestionOption;

    public StudentAttemptAnswer()
    {
        this.dateTimeOfAttempt = LocalDateTime.now();
        this.marks = 0;
    }

    public StudentAttemptAnswer(QuizQuestionOption quizQuestionOption)
    {
        this();
        this.quizQuestionOption = quizQuestionOption;
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

    public Integer getMarks()
    {
        return marks;
    }

    public void setMarks(Integer marks)
    {
        this.marks = marks;
    }

    public QuizQuestionOption getQuizQuestionOption()
    {
        return quizQuestionOption;
    }

    public void setQuizQuestionOption(QuizQuestionOption quizQuestionOption)
    {
        this.quizQuestionOption = quizQuestionOption;
    }

    @Override
    public String toString()
    {
        return "StudentAttemptAnswer{" +
                "studentAttemptAnswerId=" + studentAttemptAnswerId +
                ", quizQuestionOption=" + quizQuestionOption +
                '}';
    }
}
