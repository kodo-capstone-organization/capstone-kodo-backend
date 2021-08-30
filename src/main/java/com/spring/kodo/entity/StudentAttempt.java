package com.spring.kodo.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class StudentAttempt
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentAttemptId;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Quiz quiz;

    @OneToMany(targetEntity = StudentAttemptQuestion.class, fetch = FetchType.LAZY)
    private List<StudentAttemptQuestion> studentAttemptQuestions;

    public StudentAttempt()
    {
        this.studentAttemptQuestions = new ArrayList<>();
    }

    public Long getStudentAttemptId()
    {
        return studentAttemptId;
    }

    public void setStudentAttemptId(Long studentAttemptId)
    {
        this.studentAttemptId = studentAttemptId;
    }

    public Quiz getQuiz()
    {
        return quiz;
    }

    public void setQuiz(Quiz quiz)
    {
        this.quiz = quiz;
    }

    public List<StudentAttemptQuestion> getStudentAttemptQuestions()
    {
        return studentAttemptQuestions;
    }

    public void setStudentAttemptQuestions(List<StudentAttemptQuestion> studentAttemptQuestions)
    {
        this.studentAttemptQuestions = studentAttemptQuestions;
    }

    @Override
    public String toString()
    {
        return "StudentAttempt{" +
                "studentAttemptId=" + studentAttemptId +
                ", quiz=" + quiz +
                ", studentAttemptQuestions=" + studentAttemptQuestions +
                '}';
    }
}