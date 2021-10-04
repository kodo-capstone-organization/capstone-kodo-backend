package com.spring.kodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Locale;

@Entity(name="tag")
@Table(name="tag")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Tag
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Column(nullable = false, unique = true, length = 32)
    @NotBlank(message = "Tag title cannot be blank")
    @Size(max = 32)
    private String title;

    public Tag()
    {
    }

    public Tag(String title)
    {
        this.title = title.toLowerCase(Locale.ROOT);
    }

    public Long getTagId()
    {
        return tagId;
    }

    public void setTagId(Long tagId)
    {
        this.tagId = tagId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @Override
    public String toString()
    {
        return "Tag{" +
                "tagId=" + tagId +
                ", title='" + title + '\'' +
                '}';
    }
}
