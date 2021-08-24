package com.spring.kodo.util.exception;

public class AccountExistsException extends Exception {

    public AccountExistsException()
    {
    }

    public AccountExistsException(String message)
    {
        super(message);
    }
}
