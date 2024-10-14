package com.example.utils.service;

import com.example.domain.user.model.User;

/** Service interface for jwt service */
public interface IJwtService {
    /**
     * Generates a jwt token.
     *
     * @param user the user to generate the token for
     * @return the token
     */
    String generateToken(User user);

    /**
     * Invalidates a token.
     *
     * @param token the token to invalidate
     */
    void invalidateToken(String token);

    /**
     * Gets the username from a token.
     *
     * @param token the token
     * @return the username
     */
    String getUsernameFromToken(String token);
}
