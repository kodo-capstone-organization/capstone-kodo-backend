package com.spring.kodo.restentity.request;

import com.spring.kodo.entity.ForumPost;

public class UpdateForumPostReq {
    private ForumPost forumPost;

    public UpdateForumPostReq()
    {
    }

    public UpdateForumPostReq(ForumPost forumPost) {
        this.forumPost = forumPost;
    }

    public ForumPost getForumPost() {
        return forumPost;
    }

    public void setForumPost(ForumPost forumPost) {
        this.forumPost = forumPost;
    }
}
