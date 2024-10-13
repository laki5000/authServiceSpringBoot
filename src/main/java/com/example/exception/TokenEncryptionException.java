package com.example.exception;

/** Exception thrown when token encryption fails. */
public class TokenEncryptionException extends RuntimeException {
    /**
     * Constructs a new token encryption exception with the specified detail message.
     *
     * @param message the detail message
     */
    public TokenEncryptionException(String message, Exception e) {
        super(message.concat(": ").concat(e.getMessage()));
    }
}
