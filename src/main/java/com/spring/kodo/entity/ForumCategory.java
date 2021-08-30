package com.spring.kodo.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class ForumCategory
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long forumCategoryId;

    @Column(nullable = false, length = 128)
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 128)
    private String name;

    @Column(nullable = false, length = 256)
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 256)
    private String description;

    @OneToMany(targetEntity = ForumThread.class, fetch = FetchType.LAZY)
    @JoinColumn
    private List<ForumThread> forumThreads;

    public ForumCategory()
    {
        this.forumThreads = new ArrayList<>();
    }

    public ForumCategory(String name, String description)
    {
        this();

        this.name = name;
        this.description = description;
    }

    public Long getForumCategoryId()
    {
        return forumCategoryId;
    }

    public void setForumCategoryId(Long forumCategoryId)
    {
        this.forumCategoryId = forumCategoryId;
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

    public List<ForumThread> getForumThreads()
    {
        return forumThreads;
    }

    public void setForumThreads(List<ForumThread> forumThreads)
    {
        this.forumThreads = forumThreads;
    }

    @Override
    public String toString()
    {
        return "ForumCategory{" +
                "forumCategoryId=" + forumCategoryId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", forumThreads=" + forumThreads +
                '}';
    }
}
