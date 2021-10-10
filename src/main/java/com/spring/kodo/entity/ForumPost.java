package com.spring.kodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "forum_post")
@Table(name = "forum_post")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @Column(nullable = false)
    @NotNull
    private Boolean isReported;

    @Column(length = 1024)
    @Size(min = 0, max = 1024)
    private String reasonForReport;

    @ManyToOne(optional = true, targetEntity = ForumPost.class, fetch = FetchType.LAZY)
    private ForumPost parentForumPost;

    @OneToMany(targetEntity = ForumPost.class, fetch = FetchType.LAZY)
    private List<ForumPost> replies;

    @ManyToOne(optional = false, targetEntity = Account.class, fetch = FetchType.LAZY)
    private Account account;

    public ForumPost()
    {
        this.timeStamp = LocalDateTime.now();
        this.isReported = false;
        this.replies = new ArrayList<>();
    }

    public ForumPost(String message)
    {
        this();

        this.message = message;
    }

    public ForumPost(String message, LocalDateTime timeStamp)
    {
        this(message);

        this.timeStamp = timeStamp;
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

    public Boolean getReported()
    {
        return isReported;
    }

    public void setReported(Boolean reported)
    {
        isReported = reported;
    }

    public String getReasonForReport()
    {
        return reasonForReport;
    }

    public void setReasonForReport(String reasonForReport)
    {
        this.reasonForReport = reasonForReport;
    }

    public ForumPost getParentForumPost()
    {
        return parentForumPost;
    }

    public void setParentForumPost(ForumPost parentForumPost)
    {
        this.parentForumPost = parentForumPost;
    }

    public List<ForumPost> getReplies()
    {
        return replies;
    }

    public void setReplies(List<ForumPost> replies)
    {
        this.replies = replies;
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
                ", isReported=" + isReported +
                ", reasonForReport='" + reasonForReport + '\'' +
                ", parentForumPost=" + parentForumPost +
                ", replies=" + replies +
                ", account=" + account +
                '}';
    }
}
