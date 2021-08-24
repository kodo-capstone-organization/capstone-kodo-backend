package com.spring.kodo.entity;

import com.spring.kodo.util.enumeration.QuestionType;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table
public class Question
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private QuestionType type;

    @Column(nullable = false, length = 512)
    @NotBlank(message = "Question cannot be blank")
    @Size(max = 512)
    private String question;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    private int mark;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Quiz quiz;

    public Question()
    {
    }
}
