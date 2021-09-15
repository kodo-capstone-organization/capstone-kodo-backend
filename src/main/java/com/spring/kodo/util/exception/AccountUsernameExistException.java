package com.spring.kodo.util.exception;

public class AccountUsernameExistException extends Exception
{
    public AccountUsernameExistException()
    {
    }



    public AccountUsernameExistException(String msg)
    {
        super(msg);
    }
}
