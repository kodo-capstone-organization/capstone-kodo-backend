package com.spring.kodo.util.exception;

public class AccountPermissionDeniedException extends Exception
{
    public AccountPermissionDeniedException()
    {

    }

    public AccountPermissionDeniedException(String message)
    {
        super(message);
    }
}
