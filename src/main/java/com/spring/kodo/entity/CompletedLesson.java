package com.spring.kodo.entity;

import javax.persistence.*;

@Entity
@Table
public class CompletedLesson
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long completedLessonId;

    public CompletedLesson()
    {
    }
}
