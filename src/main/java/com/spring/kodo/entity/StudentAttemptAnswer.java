package com.spring.kodo.entity;

import javax.persistence.*;

@Entity
@Table
public class StudentAttemptAnswer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentAttemptAnswerId;

    private Integer marks;

    @ManyToOne(optional = false, targetEntity = QuizQuestionOption.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private QuizQuestionOption quizQuestionOption;

    public StudentAttemptAnswer()
    {
    }

    public StudentAttemptAnswer(QuizQuestionOption quizQuestionOption)
    {
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
