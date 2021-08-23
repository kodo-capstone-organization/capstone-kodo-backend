package com.spring.kodo.util.exception;

public class InvalidLoginCredentialsException extends Exception
{
    public InvalidLoginCredentialsException()
    {
    }


    public InvalidLoginCredentialsException(String msg)
    {
        super(msg);
    }
}
