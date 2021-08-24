package com.spring.kodo.entity;

import com.google.type.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table
public class ForumPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false, length = 1500)
    @NotBlank(message = "Message cannot be blank")
    @Size(max = 1500)
    private String message;

    @Column(nullable = false)
    @Temporal(TemporalType.TIME)
    @NotNull(message = "Timestamp cannot be blank")
    private DateTime timeStamp;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Account postCreator;

    @OneToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    private ForumPost reply;

    public ForumPost() {
    }

    public ForumPost(String message, DateTime timeStamp) {
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(DateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Account getPostCreator() {
        return postCreator;
    }

    public void setPostCreator(Account postCreator) {
        this.postCreator = postCreator;
    }

    public ForumPost getReply() {
        return reply;
    }

    public void setReply(ForumPost reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        return "ForumPost{" +
                "postId=" + postId +
                ", message='" + message + '\'' +
                ", timeStamp=" + timeStamp +
                ", postCreator=" + postCreator +
                ", reply=" + reply +
                '}';
    }
}