package com.spring.kodo.entity;

import com.spring.kodo.util.enumeration.QuestionType;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table
public class QuizQuestion
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizQuestionId;

    @Column(nullable = false, length = 2048)
    @NotBlank(message = "Content cannot be blank")
    @Size(max = 2048)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private QuestionType type;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    private int marks;

    @ManyToOne(targetEntity = Quiz.class, fetch = FetchType.LAZY)
    private Quiz quiz;

    public QuizQuestion()
    {
    }
}
