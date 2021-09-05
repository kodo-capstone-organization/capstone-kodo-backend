package com.spring.kodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table
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

    public EnrolledContent()
    {
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

    @Override
    public String toString()
    {
        return "EnrolledContent{" +
                "enrolledContentId=" + enrolledContentId +
                ", dateTimeOfCompletion=" + dateTimeOfCompletion +
                ", parentContent=" + parentContent +
                '}';
    }
}
