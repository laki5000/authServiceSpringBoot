package com.example.domain.user.service;

import static com.example.constants.MessageConstants.*;
import static com.example.constants.MessageConstants.ERROR_USER_NOT_FOUND;

import com.example.domain.user.dto.request.UserLoginRequestDTO;
import com.example.domain.user.dto.response.UserLoginResponseDTO;
import com.example.domain.user.model.User;
import com.example.domain.user.repository.IUserRepository;
import com.example.exception.InvalidCredentialsException;
import com.example.exception.NotFoundException;
import com.example.utils.service.IJwtService;
import com.example.utils.service.IMessageService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** Service class for managing user-related operations. */
@Log4j2
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {
    private final IMessageService messageService;
    private final IJwtService jwtService;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
                getByLoginAndPassword(
                        userLoginRequestDTO.getUsername(), userLoginRequestDTO.getPassword());
        String token = jwtService.generateToken(user);

        return UserLoginResponseDTO.builder().token(token).build();
    }

    /**
     * Gets a user by their login details.
     *
     * @param username the username
     * @param password the password
     * @return the user
     * @throws InvalidCredentialsException if the credentials are invalid
     */
    private User getByLoginAndPassword(String username, String password) {
        log.debug("getById called");

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            throw new InvalidCredentialsException(
                    messageService.getMessage(ERROR_USER_INVALID_CREDENTIALS));
        }

        return user.get();
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

    /**
     * Gets a user by their username.
     *
     * @param username the username
     * @return the user
     */
    public User getByUsername(String username) {
        log.debug("getByUsername called");

        return userRepository
                .findByUsername(username)
                .orElseThrow(
                        () ->
                                new NotFoundException(
                                        messageService.getMessage(ERROR_USER_NOT_FOUND)));
    }
}
