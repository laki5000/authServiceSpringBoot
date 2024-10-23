package com.example.domain.auth.controller;

import static com.example.constants.EndpointConstants.*;
import static com.example.constants.MessageConstants.SUCCESS_USER_LOGGED_IN;
import static com.example.constants.MessageConstants.SUCCESS_USER_LOGGED_OUT;

import com.example.base.dto.response.BaseResponseDTO;
import com.example.domain.auth.service.IAuthService;
import com.example.domain.user.dto.request.UserLoginRequestDTO;
import com.example.utils.dto.response.SuccessResponseDTO;
import com.example.utils.service.IMessageService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Controller class for managing user-related endpoints. */
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(AUTH_BASE_PATH)
@SuppressFBWarnings(value = "EI_EXPOSE_REP2")
public class AuthControllerImpl implements IAuthController {
    private final IMessageService messageService;
    private final IAuthService userService;

    /**
     * Logs in a user.
     *
     * @param userLoginRequestDTO the DTO containing the user's login details
     * @return the response entity
     */
    @Override
    @PostMapping(LOGIN_PATH)
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        log.info("login called");

        return ResponseEntity.ok(
                SuccessResponseDTO.builder()
                        .message(messageService.getMessage(SUCCESS_USER_LOGGED_IN))
                        .data(userService.login(userLoginRequestDTO))
                        .build());
    }

    /**
     * Logs out a user.
     *
     * @param token the token to log out
     * @return the response entity
     */
    @Override
    @PostMapping(LOGOUT_PATH)
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        log.info("logout called");

        userService.logout(token);

        return ResponseEntity.ok(
                BaseResponseDTO.builder()
                        .message(messageService.getMessage(SUCCESS_USER_LOGGED_OUT))
                        .build());
    }
}
