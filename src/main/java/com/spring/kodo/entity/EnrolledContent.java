package com.spring.kodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name="enrolled_content")
@Table(name="enrolled_content")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EnrolledContent
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrolledContentId;

    @Column(nullable = true)
    private LocalDateTime dateTimeOfCompletion;

    @ManyToOne(targetEntity = Content.class, optional = false)
    private Content parentContent;

    @OneToMany(targetEntity = StudentAttempt.class, fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "enrolled_content_id", referencedColumnName = "enrolledContentId"),
            inverseJoinColumns = @JoinColumn(name = "student_attempt_id", referencedColumnName = "studentAttemptId"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"enrolled_content_id", "student_attempt_id"})
    )
    private List<StudentAttempt> studentAttempts;

    public EnrolledContent()
    {
        this.studentAttempts = new ArrayList<>();
    }

    public EnrolledContent(Long enrolledContentId)
    {
        this();
        this.enrolledContentId = enrolledContentId;
    }

    public Long getEnrolledContentId()
    {
        return enrolledContentId;
    }

    public void setEnrolledContentId(Long enrolledContentId)
    {
        this.enrolledContentId = enrolledContentId;
    }

    public LocalDateTime getDateTimeOfCompletion()
    {
        return dateTimeOfCompletion;
    }

    public void setDateTimeOfCompletion(LocalDateTime dateTimeOfCompletion)
    {
        this.dateTimeOfCompletion = dateTimeOfCompletion;
    }

    public Content getParentContent()
    {
        return parentContent;
    }

    public void setParentContent(Content parentContent)
    {
        this.parentContent = parentContent;
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
        return "EnrolledContent{" +
                "enrolledContentId=" + enrolledContentId +
                ", dateTimeOfCompletion=" + dateTimeOfCompletion +
                ", parentContent=" + parentContent +
                ", studentAttempts=" + studentAttempts +
                '}';
    }
}
