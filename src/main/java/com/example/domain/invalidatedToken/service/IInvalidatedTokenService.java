package com.example.domain.invalidatedToken.service;

import java.time.Instant;

/** Service interface for invalidated token service. */
public interface IInvalidatedTokenService {
    /**
     * Creates an invalidated token.
     *
     * @param token the token to create
     * @param expiresAt the time at which the token expires
     */
    void create(String token, Instant expiresAt);

    /**
     * Checks if a token exists.
     *
     * @param token the token to check
     * @return true if the token exists, false otherwise
     */
    boolean existsByToken(String token);

    /**
     * Deletes expired tokens.
     *
     * @return the number of deleted tokens
     */
    int deleteExpiredTokens();
}
