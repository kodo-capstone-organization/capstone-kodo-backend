package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Content;
import com.spring.kodo.repository.ContentRepository;
import com.spring.kodo.service.inter.ContentService;
import com.spring.kodo.util.exception.ContentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentServiceImpl implements ContentService
{
    @Autowired
    private ContentRepository contentRepository;

    public ContentServiceImpl()
    {
    }

    @Override
    public Content getContentByContentId(Long contentId) throws ContentNotFoundException
    {
        Content content = contentRepository.findById(contentId).orElse(null);

        if (content != null)
        {
            return content;
        }
        else
        {
            throw new ContentNotFoundException("Content with ID: " + contentId + " does not exist!");
        }
    }
}
