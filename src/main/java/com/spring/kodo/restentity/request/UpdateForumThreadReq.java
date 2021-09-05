package com.spring.kodo.restentity.request;

import com.spring.kodo.entity.ForumThread;

public class UpdateForumThreadReq {

    private ForumThread forumThread;

    public UpdateForumThreadReq() {
    }

    public UpdateForumThreadReq(ForumThread forumThread) {
        this.forumThread = forumThread;
    }

    public ForumThread getForumThread() {
        return forumThread;
    }

    public void setForumThread(ForumThread forumThread) {
        this.forumThread = forumThread;
    }
}
