package com.spring.kodo.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Lesson
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonId;

    @Column(nullable = false, length = 128)
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 128)
    private String name;

    @Column(nullable = false, length = 512)
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 512)
    private String description;

    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer sequence;

    @OneToMany(targetEntity = Content.class, fetch = FetchType.LAZY)
    @JoinColumn
    private List<Content> contents;

    public Lesson()
    {
        this.contents = new ArrayList<>();
    }

    public Lesson(String name, String description, int sequence)
    {
        this();

        this.name = name;
        this.description = description;
        this.sequence = sequence;
    }

    public Long getLessonId()
    {
        return lessonId;
    }

    public void setLessonId(Long lessonId)
    {
        this.lessonId = lessonId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Integer getSequence()
    {
        return sequence;
    }

    public void setSequence(Integer sequence)
    {
        this.sequence = sequence;
    }

    public List<Content> getContents()
    {
        return contents;
    }

    public void setContents(List<Content> contents)
    {
        this.contents = contents;
    }

    @Override
    public String toString()
    {
        return "Lesson{" +
                "lessonId=" + lessonId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", sequence=" + sequence +
                ", contents=" + contents +
                '}';
    }
}
