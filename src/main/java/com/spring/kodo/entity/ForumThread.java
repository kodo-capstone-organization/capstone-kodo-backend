package com.spring.kodo.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class ForumThread
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long forumThreadId;

    @Column(nullable = false, length = 128)
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 128)
    private String name;

    @Column(nullable = false, length = 256)
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 256)
    private String description;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime timeStamp;

    @OneToMany(targetEntity = ForumPost.class, fetch = FetchType.LAZY)
    private List<ForumPost> forumPosts;

    public ForumThread()
    {
        this.forumPosts = new ArrayList<>();
        this.timeStamp = LocalDateTime.now();
    }

    public ForumThread(String name, String description)
    {
        this();
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getForumThreadId()
    {
        return forumThreadId;
    }

    public void setForumThreadId(Long forumThreadId)
    {
        this.forumThreadId = forumThreadId;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public LocalDateTime getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public List<ForumPost> getForumPosts()
    {
        return forumPosts;
    }

    public void setForumPosts(List<ForumPost> forumPosts)
    {
        this.forumPosts = forumPosts;
    }

    @Override
    public String toString()
    {
        return "ForumThread{" +
                "forumThreadId=" + forumThreadId +
                ", description='" + description + '\'' +
                ", timeStamp=" + timeStamp +
                ", forumPosts=" + forumPosts +
                '}';
    }
}
