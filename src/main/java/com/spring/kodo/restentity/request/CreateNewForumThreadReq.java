package com.spring.kodo.restentity.request;

import java.time.LocalDateTime;

public class CreateNewForumThreadReq {
    private String name;
    private String description;
    private LocalDateTime timeStamp;
    private Long accountId;

    public CreateNewForumThreadReq() {
    }

    public CreateNewForumThreadReq(String name, String description, LocalDateTime timeStamp, Long accountId) {
        this.name = name;
        this.description = description;
        this.timeStamp = timeStamp;
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
