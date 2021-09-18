package com.spring.kodo.restentity.response;

import com.spring.kodo.entity.Tag;

import java.util.List;

public interface TagWithAccountsCountAndCoursesCount
{
    Long getTagId();

    String getTitle();

    Integer getAccountsCount();

    Integer getCoursesCount();
}
