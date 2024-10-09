package com.example.base.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/** DTO class for the base response. */
@Getter
@SuperBuilder
@NoArgsConstructor
public class BaseResponseDTO {
    private String message;
}
