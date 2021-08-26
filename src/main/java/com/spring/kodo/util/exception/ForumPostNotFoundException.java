package com.spring.kodo.util.exception;

public class ForumPostNotFoundException extends Exception {
    public ForumPostNotFoundException() {
    }

    public ForumPostNotFoundException(String message) {
        super(message);
    }
}
