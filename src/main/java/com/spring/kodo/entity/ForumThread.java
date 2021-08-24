package com.spring.kodo.entity;

import com.google.type.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table
public class ForumThread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long threadId;

    @Column(nullable = false, length = 512)
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 512)
    private String description;

    @Column(nullable = false)
    @Temporal(TemporalType.TIME)
    @NotNull(message = "Timestamp cannot be blank")
    private DateTime timeStamp;

    @OneToMany(targetEntity = ForumPost.class, fetch = FetchType.LAZY)
    private List<ForumPost> forumPosts;

    public ForumThread() {
    }

    public ForumThread(String description, DateTime timeStamp) {
        this.description = description;
        this.timeStamp = timeStamp;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(DateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<ForumPost> getForumPosts() {
        return forumPosts;
    }

    public void setForumPosts(List<ForumPost> forumPosts) {
        this.forumPosts = forumPosts;
    }

    @Override
    public String toString() {
        return "ForumThread{" +
                "threadId=" + threadId +
                ", description='" + description + '\'' +
                ", timeStamp=" + timeStamp +
                ", forumPosts=" + forumPosts +
                '}';
    }
}
