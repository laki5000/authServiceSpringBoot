package com.example.exception;

/** Exception thrown when the credentials are invalid. */
public class InvalidCredentialsException extends RuntimeException {
    /**
     * Constructs a new invalid credentials exception with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
