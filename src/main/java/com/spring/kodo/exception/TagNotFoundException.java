package com.spring.kodo.exception;

import com.spring.kodo.exception.rest.KodoRestRuntimeException;

public class TagNotFoundException extends KodoRestRuntimeException
{
    public TagNotFoundException()
    {
        super("Tag not found");
    }

    public TagNotFoundException(String message)
    {
        super(message);
    }
}
