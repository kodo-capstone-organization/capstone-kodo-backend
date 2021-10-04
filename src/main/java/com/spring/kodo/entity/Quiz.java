package com.spring.kodo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name="quiz")
@Table(name="quiz")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "quizQuestions", "studentAttempts"})
@JsonTypeName("quiz")
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
    private List<QuizQuestion> quizQuestions;

    @OneToMany(targetEntity = StudentAttempt.class, mappedBy = "quiz", fetch = FetchType.LAZY)
    private List<StudentAttempt> studentAttempts;

    public Quiz()
    {
        this.quizQuestions = new ArrayList<>();
    }

    public Quiz(String name, String description, LocalTime timeLimit, Integer maxAttemptsPerStudent)
    {
        super(name, description);
        this.quizQuestions = new ArrayList<>();
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

    public List<QuizQuestion> getQuizQuestions()
    {
        return quizQuestions;
    }

    public void setQuizQuestions(List<QuizQuestion> questions)
    {
        this.quizQuestions = questions;
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
                ", quizQuestions=" + quizQuestions +
                ", studentAttempts=" + studentAttempts +
                '}';
    }
}
