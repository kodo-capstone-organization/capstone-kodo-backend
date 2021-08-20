package com.spring.kodo.exception;

import com.spring.kodo.exception.rest.KodoRestRuntimeException;

public class AccountNotFoundException extends KodoRestRuntimeException
{
    public AccountNotFoundException()
    {
        // TODO: Message should be passed in from thrower?
        super("Kodo account not found");
    }
}
