package com.spring.kodo.entity.rest.request;

import com.spring.kodo.entity.ForumPost;

public class CreateNewForumPostReplyReq
{

    private ForumPost newForumPostReply;
    private Long accountId;
    private Long parentForumPostId;

    public CreateNewForumPostReplyReq() {
    }

    public CreateNewForumPostReplyReq(ForumPost newForumPostReply, Long accountId, Long parentForumPostId)
    {
        this.newForumPostReply = newForumPostReply;
        this.accountId = accountId;
        this.parentForumPostId = parentForumPostId;
    }

    public ForumPost getNewForumPostReply()
    {
        return newForumPostReply;
    }

    public void setNewForumPostReply(ForumPost newForumPostReply)
    {
        this.newForumPostReply = newForumPostReply;
    }

    public Long getAccountId()
    {
        return accountId;
    }

    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }

    public Long getParentForumPostId()
    {
        return parentForumPostId;
    }

    public void setParentForumPostId(Long parentForumPostId)
    {
        this.parentForumPostId = parentForumPostId;
    }
}
