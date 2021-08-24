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
}
