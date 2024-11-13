package com.example.domain.user.dto.request;

import static com.example.constants.Constants.PASSWORD_REQUIRED_MESSAGE;
import static com.example.constants.Constants.USERNAME_REQUIRED_MESSAGE;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/** DTO class for user login request. */
@Getter
public class UserLoginRequestDTO {
    @NotBlank(message = USERNAME_REQUIRED_MESSAGE)
    private String username;

    @NotBlank(message = PASSWORD_REQUIRED_MESSAGE)
    private String password;
}
