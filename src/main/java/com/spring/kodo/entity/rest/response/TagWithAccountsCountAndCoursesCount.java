package com.spring.kodo.entity.rest.response;

public interface TagWithAccountsCountAndCoursesCount
{
    Long getTagId();

    String getTitle();

    Integer getAccountsCount();

    Integer getCoursesCount();
}
