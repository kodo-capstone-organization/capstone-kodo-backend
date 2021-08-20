package com.spring.kodo.exception.rest;

import org.springframework.web.bind.annotation.ResponseStatus;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class KodoRestRuntimeException extends RuntimeException
{
    public KodoRestRuntimeException(String message)
    {
        super(message);
        System.out.println(message);
    }
}
