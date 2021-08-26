package com.spring.kodo.service;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Content;
import com.spring.kodo.util.exception.AccountNotFoundException;
import com.spring.kodo.util.exception.ContentNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.UnknownPersistenceException;

import java.util.List;

public interface ContentService
{
    Content createNewContent(Content content) throws InputDataValidationException, UnknownPersistenceException;

    Content getContentByContentId(Long contentId) throws ContentNotFoundException;

    Content getContentByName(String name) throws ContentNotFoundException;

    List<Content> getAllContents();
}
