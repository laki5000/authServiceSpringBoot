package com.example.domain.invalidatedToken.service;

/** Service interface for invalidated token cleanup scheduler. */
public interface IInvalidatedTokenCleanupScheduler {
    /** Cleans up invalid tokens. */
    void cleanUpInvalidTokens();
}
