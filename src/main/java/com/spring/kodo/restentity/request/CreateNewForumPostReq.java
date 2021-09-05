package com.spring.kodo.restentity.request;

import java.time.LocalDateTime;

public class CreateNewForumPostReq {

    private String message;
    private LocalDateTime timeStamp;
    private Long accountId;

    public CreateNewForumPostReq() {
    }

    public CreateNewForumPostReq(String message, LocalDateTime timeStamp, Long accountId) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.accountId = accountId;
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
}
