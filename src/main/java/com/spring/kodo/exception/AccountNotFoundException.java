package com.spring.kodo.exception;

import com.spring.kodo.exception.rest.KodoRestRuntimeException;

public class AccountNotFoundException extends KodoRestRuntimeException
{
    public AccountNotFoundException()
    {
        super("Account not found");
    }

    public AccountNotFoundException(String message)
    {
        super(message);
    }
}
