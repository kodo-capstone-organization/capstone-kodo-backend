package com.spring.kodo.util.exception;

public class AccountEmailExistException extends Exception
{
    public AccountEmailExistException()
    {
    }



    public AccountEmailExistException(String msg)
    {
        super(msg);
    }
}
