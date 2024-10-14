package com.example.exception;

/** Exception thrown when a resource is not found. */
public class NotFoundException extends RuntimeException {
    /**
     * Constructs a new not found exception with the specified detail message.
     *
     * @param message the detail message
     */
    public NotFoundException(String message) {
        super(message);
    }
}
