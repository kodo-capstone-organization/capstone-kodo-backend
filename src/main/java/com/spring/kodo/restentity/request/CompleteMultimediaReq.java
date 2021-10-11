package com.spring.kodo.restentity.request;

public class CompleteMultimediaReq
{
    private Boolean complete;
    private Long enrolledContentId;

    public CompleteMultimediaReq()
    {
    }

    public CompleteMultimediaReq(Boolean complete, Long enrolledContentId)
    {
        this.complete = complete;
        this.enrolledContentId = enrolledContentId;
    }

    public Boolean getComplete()
    {
        return complete;
    }

    public void setComplete(Boolean complete)
    {
        this.complete = complete;
    }

    public Long getEnrolledContentId()
    {
        return enrolledContentId;
    }

    public void setEnrolledContentId(Long enrolledContentId)
    {
        this.enrolledContentId = enrolledContentId;
    }
}
