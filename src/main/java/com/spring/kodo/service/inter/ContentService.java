package com.spring.kodo.service.inter;

import com.spring.kodo.entity.Content;
import com.spring.kodo.util.exception.ContentNotFoundException;

public interface ContentService
{
    Content getContentByContentId(Long contentId) throws ContentNotFoundException;
}
