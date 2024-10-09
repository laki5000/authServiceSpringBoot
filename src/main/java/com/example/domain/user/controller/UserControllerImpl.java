package com.example.domain.user.controller;

import static com.example.constants.EndpointConstants.*;

import com.example.domain.user.service.IUserService;
import com.example.utils.service.IMessageService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
}
