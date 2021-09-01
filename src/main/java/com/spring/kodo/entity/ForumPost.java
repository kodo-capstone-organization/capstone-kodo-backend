package com.spring.kodo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table
public class ForumPost
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long forumPostId;

    @Column(nullable = false, length = 2048)
    @NotBlank(message = "Message cannot be blank")
    @Size(max = 2048)
    private String message;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime timeStamp;

    @OneToOne(optional = true, targetEntity = ForumPost.class, fetch = FetchType.LAZY)
    private ForumPost reply;

    @ManyToOne(optional = false)
    private Account account;

    public ForumPost()
    {
        this.timeStamp = LocalDateTime.now();
    }

    public ForumPost(String message)
    {
        this();

        this.message = message;
    }

    public Long getForumPostId()
    {
        return forumPostId;
    }

    public void setForumPostId(Long forumPostId)
    {
        this.forumPostId = forumPostId;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public LocalDateTime getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public ForumPost getReply()
    {
        return reply;
    }

    public void setReply(ForumPost reply)
    {
        this.reply = reply;
    }

    public Account getAccount()
    {
        return account;
    }

    public void setAccount(Account account)
    {
        this.account = account;
    }

    @Override
    public String toString()
    {
        return "ForumPost{" +
                "forumPostId=" + forumPostId +
                ", message='" + message + '\'' +
                ", timeStamp=" + timeStamp +
                ", reply=" + reply +
                ", account=" + account +
                '}';
    }
}
