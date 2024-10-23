package com.example.domain.user.service;

import com.example.domain.user.model.User;

/** Service interface for user service. */
public interface IUserService {
    /**
     * Gets a user by their login details.
     *
     * @param username the username
     * @param password the password
     * @return the user
     */
    User getByUsernameAndPassword(String username, String password);

    /**
     * Gets a user by their username.
     *
     * @param username the username
     * @return the user
     */
    User getByUsername(String username);
}
