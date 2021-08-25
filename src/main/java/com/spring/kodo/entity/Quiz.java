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

    @OneToMany(targetEntity = StudentAttempt.class, mappedBy = "quiz", fetch = FetchType.LAZY)
    private List<StudentAttempt> studentAttempts;

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

    public LocalTime getTimeLimit()
    {
        return timeLimit;
    }

    public void setTimeLimit(LocalTime timeLimit)
    {
        this.timeLimit = timeLimit;
    }

    public List<QuizQuestion> getQuestions()
    {
        return questions;
    }

    public void setQuestions(List<QuizQuestion> questions)
    {
        this.questions = questions;
    }

    public List<StudentAttempt> getStudentAttempts()
    {
        return studentAttempts;
    }

    public void setStudentAttempts(List<StudentAttempt> studentAttempts)
    {
        this.studentAttempts = studentAttempts;
    }

    @Override
    public String toString()
    {
        return "Quiz{" +
                "timeLimit=" + timeLimit +
                ", questions=" + questions +
                ", studentAttempts=" + studentAttempts +
                '}';
    }
}
