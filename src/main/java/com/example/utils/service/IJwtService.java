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
}
