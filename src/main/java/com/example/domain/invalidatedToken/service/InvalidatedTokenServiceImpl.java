package com.example.domain.invalidatedToken.service;

import com.example.domain.invalidatedToken.model.InvalidatedToken;
import com.example.domain.invalidatedToken.repository.IInvalidatedTokenRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/** Service class for managing invalidated token-related operations. */
@Log4j2
@RequiredArgsConstructor
@Service
public class InvalidatedTokenServiceImpl implements IInvalidatedTokenService {
    private final IInvalidatedTokenRepository invalidatedTokenRepository;

    /**
     * Creates an invalidated token.
     *
     * @param token the token to create
     * @param expiresAt the time at which the token expires
     */
    @Override
    public void create(String token, Instant expiresAt) {
        log.debug("create called");

        invalidatedTokenRepository.save(
                InvalidatedToken.builder().token(token).expiresAt(expiresAt).build());
    }

    /**
     * Checks if a token exists.
     *
     * @param token the token to check
     * @return true if the token exists, false otherwise
     */
    @Override
    public boolean existsByToken(String token) {
        log.debug("existsByToken called");

        return invalidatedTokenRepository.existsByToken(token);
    }

    /**
     * Deletes expired tokens.
     *
     * @return the number of deleted tokens
     */
    public int deleteExpiredTokens() {
        log.debug("deleteExpiredTokens called");

        Instant now = Instant.now();

        return invalidatedTokenRepository.deleteByExpiresAtBefore(now);
    }
}
