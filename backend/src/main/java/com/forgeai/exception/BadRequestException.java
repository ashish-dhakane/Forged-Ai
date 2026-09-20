package com.forgeai.exception;

// Thrown when an incoming API request violates domain rules or validation criteria.
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
