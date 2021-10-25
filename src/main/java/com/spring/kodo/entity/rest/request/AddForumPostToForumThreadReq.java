package com.spring.kodo.entity.rest.request;

import com.spring.kodo.entity.ForumPost;
import com.spring.kodo.entity.ForumThread;

public class AddForumPostToForumThreadReq {
    private ForumPost forumPost;
    private ForumThread forumThread;

    public AddForumPostToForumThreadReq() {
    }

    public AddForumPostToForumThreadReq(ForumPost forumPost, ForumThread forumThread) {
        this.forumPost = forumPost;
        this.forumThread = forumThread;
    }

    public ForumPost getForumPost() {
        return forumPost;
    }

    public void setForumPost(ForumPost forumPost) {
        this.forumPost = forumPost;
    }

    public ForumThread getForumThread() {
        return forumThread;
    }

    public void setForumThread(ForumThread forumThread) {
        this.forumThread = forumThread;
    }
}
