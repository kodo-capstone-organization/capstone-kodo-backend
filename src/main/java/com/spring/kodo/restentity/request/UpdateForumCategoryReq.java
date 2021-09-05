package com.spring.kodo.restentity.request;

import com.spring.kodo.entity.ForumCategory;

public class UpdateForumCategoryReq {
    private ForumCategory forumCategory;

    public UpdateForumCategoryReq() {
    }

    public UpdateForumCategoryReq(ForumCategory forumCategory) {
        this.forumCategory = forumCategory;
    }

    public ForumCategory getForumCategory() {
        return forumCategory;
    }

    public void setForumCategory(ForumCategory forumCategory) {
        this.forumCategory = forumCategory;
    }
}
