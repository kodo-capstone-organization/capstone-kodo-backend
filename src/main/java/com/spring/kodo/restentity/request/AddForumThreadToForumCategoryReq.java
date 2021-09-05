package com.spring.kodo.restentity.request;

import com.spring.kodo.entity.ForumCategory;
import com.spring.kodo.entity.ForumThread;

public class AddForumThreadToForumCategoryReq {

    private ForumThread forumThread;
    private ForumCategory forumCategory;

    public AddForumThreadToForumCategoryReq() {
    }

    public AddForumThreadToForumCategoryReq(ForumThread forumThread, ForumCategory forumCategory) {
        this.forumThread = forumThread;
        this.forumCategory = forumCategory;
    }

    public ForumThread getForumThread() {
        return forumThread;
    }

    public void setForumThread(ForumThread forumThread) {
        this.forumThread = forumThread;
    }

    public ForumCategory getForumCategory() {
        return forumCategory;
    }

    public void setForumCategory(ForumCategory forumCategory) {
        this.forumCategory = forumCategory;
    }
}
