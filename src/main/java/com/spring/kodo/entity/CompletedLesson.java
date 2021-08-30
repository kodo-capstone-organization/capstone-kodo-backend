package com.spring.kodo.entity;

import javax.persistence.*;

@Entity
@Table
public class CompletedLesson
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long completedLessonId;

    @ManyToOne(targetEntity = Lesson.class, optional = false)
    private Lesson parentLesson;

    public CompletedLesson()
    {
    }

    public Long getCompletedLessonId()
    {
        return completedLessonId;
    }

    public void setCompletedLessonId(Long completedLessonId)
    {
        this.completedLessonId = completedLessonId;
    }

    public Lesson getParentLesson()
    {
        return parentLesson;
    }

    public void setParentLesson(Lesson parentLesson)
    {
        this.parentLesson = parentLesson;
    }

    @Override
    public String toString()
    {
        return "CompletedLesson{" +
                "completedLessonId=" + completedLessonId +
                ", parentLesson=" + parentLesson +
                '}';
    }
}
