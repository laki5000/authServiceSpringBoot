package com.example.utils.service;

import static com.example.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

/** Unit tests for {@link MessageServiceImpl}. */
@ExtendWith(MockitoExtension.class)
public class MessageServiceTests {
    @InjectMocks private MessageServiceImpl messageService;
    @Mock private MessageSource messageSource;
    @Mock private LocaleResolver localeResolver;
    @Mock private HttpServletRequest request;
    @Mock private ServletRequestAttributes servletRequestAttributes;

    /** Sets up the test environment before each test. */
    @BeforeEach
    public void setUp() {
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);

        when(servletRequestAttributes.getRequest()).thenReturn(request);
    }

    @Test
    @DisplayName("Tests the successful retrieval of a message.")
    public void getMessage_Success() {
        // Given
        when(localeResolver.resolveLocale(request)).thenReturn(TEST_LOCALE);
        when(messageSource.getMessage(TEST_KEY, null, TEST_LOCALE)).thenReturn(TEST_MESSAGE);

        // When
        String result = messageService.getMessage(TEST_KEY);

        // Then
        assertEquals(TEST_MESSAGE, result);
    }

    @Test
    @DisplayName("Tests the unsuccessful retrieval of a message due to null attributes.")
    public void getMessage_NullAttributes() {
        // Given
        when(servletRequestAttributes.getRequest()).thenReturn(null);

        // When
        String result = messageService.getMessage(TEST_KEY);

        // Then
        assertNull(result);
    }

    @Test
    @DisplayName("Tests the unsuccessful retrieval of a message due to an invalid key.")
    public void getMessage_InvalidKey() {
        // Given
        when(localeResolver.resolveLocale(request)).thenReturn(TEST_LOCALE);
        when(messageSource.getMessage(TEST_KEY, null, TEST_LOCALE)).thenReturn(null);

        // When
        String result = messageService.getMessage(TEST_KEY);

        // Then
        assertNull(result);
    }
}
