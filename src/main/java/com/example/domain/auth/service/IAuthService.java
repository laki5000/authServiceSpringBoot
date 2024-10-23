package com.example.domain.auth.service;

import com.example.domain.user.dto.request.UserLoginRequestDTO;
import com.example.domain.user.dto.response.UserLoginResponseDTO;

/** Service interface for auth service. */
public interface IAuthService {
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
}
