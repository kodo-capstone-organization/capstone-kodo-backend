package com.spring.kodo.restentity.request;

public class CompleteMultimediaReq
{
    private Boolean complete;
    private Long accountId;
    private Long contentId;

    public CompleteMultimediaReq()
    {
    }

    public CompleteMultimediaReq(Boolean complete, Long accountId, Long contentId)
    {
        this.complete = complete;
        this.accountId = accountId;
        this.contentId = contentId;
    }

    public Boolean getComplete()
    {
        return complete;
    }

    public void setComplete(Boolean complete)
    {
        this.complete = complete;
    }

    public Long getAccountId()
    {
        return accountId;
    }

    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }

    public Long getContentId()
    {
        return contentId;
    }

    public void setContentId(Long contentId)
    {
        this.contentId = contentId;
    }
}
