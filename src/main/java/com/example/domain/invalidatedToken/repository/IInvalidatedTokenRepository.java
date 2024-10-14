package com.example.domain.invalidatedToken.repository;

import com.example.domain.invalidatedToken.model.InvalidatedToken;
import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository interface for invalidated token-related operations. */
@Repository
public interface IInvalidatedTokenRepository extends JpaRepository<InvalidatedToken, Long> {
    /**
     * Checks if a token exists.
     *
     * @param token the token
     * @return true if the token exists, false otherwise
     */
    boolean existsByToken(String token);

    /**
     * Deletes tokens that have expired.
     *
     * @return the number of deleted tokens
     */
    int deleteByExpiresAtBefore(Instant expiresAt);
}
