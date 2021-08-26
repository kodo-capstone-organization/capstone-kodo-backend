package com.spring.kodo.util.exception;

public class AccountUsernameOrEmailExistsException extends Exception
{
    public AccountUsernameOrEmailExistsException()
    {
    }



    public AccountUsernameOrEmailExistsException(String msg)
    {
        super(msg);
    }
}
