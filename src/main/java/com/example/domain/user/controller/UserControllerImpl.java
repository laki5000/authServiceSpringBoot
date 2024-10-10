package com.example.domain.user.controller;

import static com.example.constants.EndpointConstants.*;
import static com.example.constants.MessageConstants.SUCCESS_USER_LOGGED_IN;

import com.example.domain.user.dto.request.UserLoginRequestDTO;
import com.example.domain.user.service.IUserService;
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
@RequestMapping(USER_BASE_URL)
@SuppressFBWarnings(value = "EI_EXPOSE_REP2")
public class UserControllerImpl implements IUserController {
    private final IMessageService messageService;
    private final IUserService userService;

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
}
