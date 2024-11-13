package com.example.domain.user.service;

import static com.example.constants.Constants.ERROR_USER_INVALID_CREDENTIALS;
import static com.example.constants.Constants.ERROR_USER_NOT_FOUND;

import com.example.domain.user.model.User;
import com.example.domain.user.repository.IUserRepository;
import com.example.exception.InvalidCredentialsException;
import com.example.exception.NotFoundException;
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
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Gets a user by their login details.
     *
     * @param username the username
     * @param password the password
     * @return the user
     * @throws InvalidCredentialsException if the credentials are invalid
     */
    @Override
    public User getByUsernameAndPassword(String username, String password) {
        log.debug("getByUsernameAndPassword called");

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            throw new InvalidCredentialsException(
                    messageService.getMessage(ERROR_USER_INVALID_CREDENTIALS));
        }

        return user.get();
    }

    /**
     * Gets a user by their username.
     *
     * @param username the username
     * @return the user
     */
    @Override
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
