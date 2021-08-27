package com.spring.kodo.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Content
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentId;

    @Column(nullable = false, length = 64)
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 64)
    private String name;

    @Column(nullable = false, length = 512)
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 512)
    private String description;

    public Content()
    {
    }

    public Content(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public Long getContentId()
    {
        return contentId;
    }

    public void setContentId(Long contentId)
    {
        this.contentId = contentId;
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

    @Override
    public String toString()
    {
        return "Content{" +
                "contentId=" + contentId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
