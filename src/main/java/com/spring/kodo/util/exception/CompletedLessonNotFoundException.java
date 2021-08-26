package com.spring.kodo.util.exception;


public class CompletedLessonNotFoundException extends Exception
{
    public CompletedLessonNotFoundException()
    {

    }

    public CompletedLessonNotFoundException(String message)
    {
        super(message);
    }
}
