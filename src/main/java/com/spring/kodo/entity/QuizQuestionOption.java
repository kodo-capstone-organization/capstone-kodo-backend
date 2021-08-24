package com.spring.kodo.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table
public class QuizQuestionOption
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizQuestionOptionId;

    @Column(nullable = false, length = 512)
    @NotBlank(message = "Left Content cannot be blank")
    @Size(max = 512)
    private String leftContent;

    @Column(nullable = false, length = 512)
    @Size(max = 512)
    private String rightContent;

    @Column(nullable = false)
    @NotNull
    private Boolean correct;
}
