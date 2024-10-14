package com.example.domain.invalidatedToken.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Service class for managing the cleanup of invalidated tokens. */
@Log4j2
@RequiredArgsConstructor
@Component
public class InvalidatedTokenCleanupSchedulerImpl implements IInvalidatedTokenCleanupScheduler {
    private final IInvalidatedTokenService invalidatedTokenService;

    /** Scheduled method to clean up invalidated tokens. */
    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanUpInvalidTokens() {
        log.info("cleanUpInvalidTokens called");

        int deletedTokens = invalidatedTokenService.deleteExpiredTokens();

        log.info("{} invalidated tokens have been deleted.", deletedTokens);
    }
}
