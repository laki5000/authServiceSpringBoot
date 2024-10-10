package com.example.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import static com.example.constants.ValidationConstants.PASSWORD_REQUIRED_MESSAGE;
import static com.example.constants.ValidationConstants.USERNAME_REQUIRED_MESSAGE;

/** DTO class for user login request. */
@Getter
public class UserLoginRequestDTO {
    @NotNull(message = USERNAME_REQUIRED_MESSAGE)
    private String username;

    @NotNull(message = PASSWORD_REQUIRED_MESSAGE)
    private String password;
}
