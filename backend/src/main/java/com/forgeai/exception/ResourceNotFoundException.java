package com.forgeai.exception;

// Thrown when a requested database entity or resource cannot be found.
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
