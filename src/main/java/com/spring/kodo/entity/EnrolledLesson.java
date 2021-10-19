package com.spring.kodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name="enrolled_lesson")
@Table(name="enrolled_lesson")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EnrolledLesson
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrolledLessonId;

    @Column(nullable = true)
    private LocalDateTime dateTimeOfCompletion;

    @ManyToOne(targetEntity = Lesson.class, optional = true)
    private Lesson parentLesson;

    @OneToMany(targetEntity = EnrolledContent.class, fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "enrolled_lesson_id", referencedColumnName = "enrolledLessonId"),
            inverseJoinColumns = @JoinColumn(name = "enrolled_content_id", referencedColumnName = "enrolledContentId"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"enrolled_lesson_id", "enrolled_content_id"})
    )
    private List<EnrolledContent> enrolledContents;

    public EnrolledLesson()
    {
        this.enrolledContents = new ArrayList<>();
    }

    public EnrolledLesson(Long enrolledLessonId)
    {
        this();
        this.enrolledLessonId = enrolledLessonId;
    }

    public Long getEnrolledLessonId()
    {
        return enrolledLessonId;
    }

    public void setEnrolledLessonId(Long enrolledLessonId)
    {
        this.enrolledLessonId = enrolledLessonId;
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

    public List<EnrolledContent> getEnrolledContents()
    {
        return enrolledContents;
    }

    public void setEnrolledContents(List<EnrolledContent> enrolledContents)
    {
        this.enrolledContents = enrolledContents;
    }

    @Override
    public String toString()
    {
        return "EnrolledLesson{" +
                "enrolledLessonId=" + enrolledLessonId +
                ", dateTimeOfCompletion=" + dateTimeOfCompletion +
                ", parentLesson=" + parentLesson +
                ", enrolledContents=" + enrolledContents +
                '}';
    }
}
