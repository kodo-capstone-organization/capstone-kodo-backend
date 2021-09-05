package com.spring.kodo.restentity.request;

public class CreateNewForumCategoryReq {

    private String name;
    private String description;
    private Long courseId;

    public CreateNewForumCategoryReq() {
    }

    public CreateNewForumCategoryReq(String name, String description, Long courseId) {
        this.name = name;
        this.description = description;
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
