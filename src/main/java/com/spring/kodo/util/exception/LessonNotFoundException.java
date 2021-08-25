package com.spring.kodo.util.exception;

public class LessonNotFoundException extends Exception {
    public LessonNotFoundException() {}

    public LessonNotFoundException(String message) {
        super(message);
    }
}

