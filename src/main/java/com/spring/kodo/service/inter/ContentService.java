package com.spring.kodo.service.inter;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Content;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface ContentService
{
    Content getContentByContentId(Long contentId) throws ContentNotFoundException;

    Content getContentByName(String name) throws ContentNotFoundException;

    List<Content> getAllContents();
}
