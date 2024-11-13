package com.example.utils.service;

import com.example.domain.invalidatedToken.service.IInvalidatedTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Unit tests for {@link JwtServiceImpl}. */
@ExtendWith(MockitoExtension.class)
public class JwtServiceTests {
    @InjectMocks private JwtServiceImpl jwtService;
    @Mock private IMessageService messageService;
    @Mock private IInvalidatedTokenService invalidatedTokenService;

    @Test
    public void generateToken_Success() {}
}
