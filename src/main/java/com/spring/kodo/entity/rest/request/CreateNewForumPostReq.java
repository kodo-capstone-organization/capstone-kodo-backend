package com.spring.kodo.entity.rest.request;

import java.time.LocalDateTime;

public class CreateNewForumPostReq {

    private String message;
    private LocalDateTime timeStamp;
    private Long accountId;
    private Long forumThreadId;


    public CreateNewForumPostReq() {
    }

    public CreateNewForumPostReq(String message, LocalDateTime timeStamp, Long accountId, Long forumThreadId) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.accountId = accountId;
        this.forumThreadId = forumThreadId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccount(Long accountId) {
        this.accountId = accountId;
    }

    public Long getForumThreadId() {
        return forumThreadId;
    }

    public void setForumThreadId(Long forumThreadId) {
        this.forumThreadId = forumThreadId;
    }
}
