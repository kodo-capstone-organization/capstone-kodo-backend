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

    @OneToMany(targetEntity = Question.class, mappedBy = "quiz", fetch = FetchType.LAZY)
    private List<Question> questions;

    public Quiz()
    {
        this.questions = new ArrayList<>();
    }

    public Quiz(String name, String description, LocalTime timeLimit)
    {
        super(name, description);
        this.timeLimit = timeLimit;
        this.questions = new ArrayList<>();
    }

    public LocalTime getTimeLimit()
    {
        return timeLimit;
    }

    public void setTimeLimit(LocalTime timeLimit)
    {
        this.timeLimit = timeLimit;
    }

    public List<Question> getQuestions()
    {
        return questions;
    }

    public void setQuestions(List<Question> questions)
    {
        this.questions = questions;
    }

    @Override
    public String toString()
    {
        return "Quiz{" +
                "timeLimit=" + timeLimit +
                ", questions=" + questions +
                '}';
    }
}
