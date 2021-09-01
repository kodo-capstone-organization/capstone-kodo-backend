package com.spring.kodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CompletedLesson
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long completedLessonId;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime dateTimeOfCompletion;

    @ManyToOne(targetEntity = Lesson.class, optional = false)
    private Lesson parentLesson;

    public CompletedLesson()
    {
        this.dateTimeOfCompletion = LocalDateTime.now();
    }

    public Long getCompletedLessonId()
    {
        return completedLessonId;
    }

    public void setCompletedLessonId(Long completedLessonId)
    {
        this.completedLessonId = completedLessonId;
    }

    public LocalDateTime getDateTimeOfCompletion()
    {
        return dateTimeOfCompletion;
    }

    public void setDateTimeOfCompletion(LocalDateTime dateTimeOfCompletion)
    {
        this.dateTimeOfCompletion = dateTimeOfCompletion;
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
                ", dateTimeOfCompletion=" + dateTimeOfCompletion +
                ", parentLesson=" + parentLesson +
                '}';
    }
}
