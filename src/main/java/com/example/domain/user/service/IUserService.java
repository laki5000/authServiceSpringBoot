package com.example.domain.user.service;

import com.example.domain.user.dto.request.UserLoginRequestDTO;
import com.example.domain.user.dto.response.UserLoginResponseDTO;

/** Service interface for user service. */
public interface IUserService {
    /**
     * Logs in a user.
     *
     * @param userLoginRequestDTO the DTO containing the user's login details
     * @return the response DTO
     */
    UserLoginResponseDTO login(UserLoginRequestDTO userLoginRequestDTO);
}
