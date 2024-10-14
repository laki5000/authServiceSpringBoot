package com.example.domain.user.service;

import com.example.domain.user.dto.request.UserLoginRequestDTO;
import com.example.domain.user.dto.response.UserLoginResponseDTO;
import com.example.domain.user.model.User;

/** Service interface for user service. */
public interface IUserService {
    /**
     * Logs in a user.
     *
     * @param userLoginRequestDTO the DTO containing the user's login details
     * @return the response DTO
     */
    UserLoginResponseDTO login(UserLoginRequestDTO userLoginRequestDTO);

    /**
     * Logs out a user.
     *
     * @param token the token to log out
     */
    void logout(String token);

    /**
     * Gets a user by their username.
     *
     * @param username the username
     * @return the user
     */
    User getByUsername(String username);
}
