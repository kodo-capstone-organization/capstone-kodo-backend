package com.spring.kodo.restentity.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ForumPostWithRepliesResp
{
    private Long forumPostId;
    private String message;
    private LocalDateTime timeStamp;
    private List<ForumPostWithRepliesResp> replies;

    public ForumPostWithRepliesResp()
    {
        this.replies = new ArrayList<>();
    }

    public ForumPostWithRepliesResp(Long forumPostId, String message, LocalDateTime timeStamp)
    {
        this();

        this.forumPostId = forumPostId;
        this.message = message;
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

    public List<ForumPostWithRepliesResp> getReplies()
    {
        return replies;
    }

    public void setReplies(List<ForumPostWithRepliesResp> replies)
    {
        this.replies = replies;
    }
}
