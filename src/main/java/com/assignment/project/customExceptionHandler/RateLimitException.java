package com.assignment.project.customExceptionHandler;

public class RateLimitException extends RuntimeException {

    public RateLimitException(String message) {
        super(message);
    }
}