package com.spring.kodo.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @Column(nullable = false)
    @NotNull
    @Min(0)
    @Max(100)
    private Integer maxAttemptsPerStudent;

    @OneToMany(targetEntity = QuizQuestion.class, mappedBy = "quiz", fetch = FetchType.LAZY)
    private List<QuizQuestion> questions;

    @OneToMany(targetEntity = StudentAttempt.class, mappedBy = "quiz", fetch = FetchType.LAZY)
    private List<StudentAttempt> studentAttempts;

    public Quiz()
    {
        this.questions = new ArrayList<>();
    }

    public Quiz(String name, String description, LocalTime timeLimit, Integer maxAttemptsPerStudent)
    {
        super(name, description);
        this.questions = new ArrayList<>();
        this.timeLimit = timeLimit;
        this.maxAttemptsPerStudent = maxAttemptsPerStudent;
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

    public Integer getMaxAttemptsPerStudent() {
        return maxAttemptsPerStudent;
    }

    public void setMaxAttemptsPerStudent(Integer maxAttemptsPerStudent) {
        this.maxAttemptsPerStudent = maxAttemptsPerStudent;
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
