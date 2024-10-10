package com.example.domain.user.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UserLoginResponseDTO {
    private String token;
}
