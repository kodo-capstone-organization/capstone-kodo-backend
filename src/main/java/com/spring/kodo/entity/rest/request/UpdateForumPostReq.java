package com.spring.kodo.entity.rest.request;

import com.spring.kodo.entity.ForumPost;

public class UpdateForumPostReq {
    private ForumPost forumPost;
    private String reasonForReport;

    public UpdateForumPostReq()
    {
    }

    public UpdateForumPostReq(ForumPost forumPost, String reasonForReport)
    {
        this.forumPost = forumPost;
        this.reasonForReport = reasonForReport;
    }

    public ForumPost getForumPost()
    {
        return forumPost;
    }

    public void setForumPost(ForumPost forumPost)
    {
        this.forumPost = forumPost;
    }

    public String getReasonForReport()
    {
        return reasonForReport;
    }

    public void setReasonForReport(String reasonForReport)
    {
        this.reasonForReport = reasonForReport;
    }
}
