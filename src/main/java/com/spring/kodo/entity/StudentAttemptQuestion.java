package com.spring.kodo.entity;

import javax.persistence.*;

@Entity
@Table
public class StudentAttemptQuestion
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentAttemptQuestionId;

    @ManyToOne(optional = false, targetEntity = QuizQuestion.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private QuizQuestion quizQuestion;

    public StudentAttemptQuestion()
    {
    }
}
