package com.spring.kodo.entity.rest.request;

import java.util.List;

public class CreateNewTagsReq {
        private List<String> tags;

    public CreateNewTagsReq() {
    }

    public CreateNewTagsReq(List<String> tags) {
        this.setTags(tags);
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
