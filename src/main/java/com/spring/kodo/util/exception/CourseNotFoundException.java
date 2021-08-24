package com.spring.kodo.util.exception;


public class CourseNotFoundException extends Exception
{
    public CourseNotFoundException()
    {

    }

    public CourseNotFoundException(String message)
    {
        super(message);
    }
}
