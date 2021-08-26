package com.spring.kodo.service;

import com.spring.kodo.entity.Content;
import com.spring.kodo.util.exception.ContentNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;

import java.util.List;

public interface ContentService
{
    Content createNewContent(Content content) throws InputDataValidationException;

    List<Content> getAllContents();
    Content getContentByName(String name) throws ContentNotFoundException;
}
