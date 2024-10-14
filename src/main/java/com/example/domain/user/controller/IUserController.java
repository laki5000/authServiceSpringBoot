package com.example.domain.user.controller;

import com.example.domain.user.dto.request.UserLoginRequestDTO;
import org.springframework.http.ResponseEntity;

/** Controller interface for user controller. */
public interface IUserController {
    /**
     * Logs in a user.
     *
     * @param userLoginRequestDTO the DTO containing the user's login details
     * @return the response entity
     */
    ResponseEntity<?> login(UserLoginRequestDTO userLoginRequestDTO);

    /**
     * Logs out a user.
     *
     * @param token the token to log out
     * @return the response entity
     */
    ResponseEntity<?> logout(String token);
}
