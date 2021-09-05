package com.spring.kodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EnrolledLesson
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrolledLessonId;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime dateTimeOfCompletion;

    @ManyToOne(targetEntity = Lesson.class, optional = false)
    private Lesson parentLesson;

    public EnrolledLesson()
    {
        this.dateTimeOfCompletion = LocalDateTime.now();
    }

    public Long getEnrolledLessonId()
    {
        return enrolledLessonId;
    }

    public void setEnrolledLessonId(Long completedLessonId)
    {
        this.enrolledLessonId = completedLessonId;
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
        return "EnrolledLesson{" +
                "enrolledLessonId=" + enrolledLessonId +
                ", dateTimeOfCompletion=" + dateTimeOfCompletion +
                ", parentLesson=" + parentLesson +
                '}';
    }
}
