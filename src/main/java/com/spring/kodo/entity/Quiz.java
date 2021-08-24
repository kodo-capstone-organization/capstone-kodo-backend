package com.spring.kodo.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Quiz extends Content
{
    @Column(nullable = false)
    @NotNull
    private LocalTime timeLimit;

    @OneToMany(targetEntity = QuizQuestion.class, mappedBy = "quiz", fetch = FetchType.LAZY)
    private List<QuizQuestion> questions;

    public Quiz()
    {
        this.questions = new ArrayList<>();
    }

    public Quiz(String name, String description, LocalTime timeLimit)
    {
        super(name, description);
        this.questions = new ArrayList<>();
        this.timeLimit = timeLimit;
    }
}
