package com.example.domain.auth.service;

import com.example.domain.user.dto.request.UserLoginRequestDTO;
import com.example.domain.user.dto.response.UserLoginResponseDTO;
import com.example.domain.user.model.User;
import com.example.domain.user.service.IUserService;
import com.example.exception.InvalidCredentialsException;
import com.example.utils.service.IJwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/** Service class for managing auth-related operations. */
@Log4j2
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements IAuthService {
    private final IUserService userService;
    private final IJwtService jwtService;

    /**
     * Logs in a user.
     *
     * @param userLoginRequestDTO the DTO containing the user's login details
     * @return the response DTO
     * @throws InvalidCredentialsException if the credentials are invalid
     */
    @Override
    public UserLoginResponseDTO login(UserLoginRequestDTO userLoginRequestDTO) {
        log.debug("login called");

        User user =
                userService.getByUsernameAndPassword(
                        userLoginRequestDTO.getUsername(), userLoginRequestDTO.getPassword());
        String token = jwtService.generateToken(user);

        return UserLoginResponseDTO.builder().token(token).build();
    }

    /**
     * Logs out a user.
     *
     * @param token the token to log out
     */
    @Override
    public void logout(String token) {
        log.debug("logout called");

        jwtService.invalidateToken(token);
    }
}
