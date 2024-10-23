package com.example.security;

import static com.example.constants.EndpointConstants.AUTH_BASE_PATH;
import static com.example.constants.EndpointConstants.LOGIN_PATH;
import static com.example.constants.MessageConstants.ERROR_DEFAULT_MESSAGE;
import static com.example.constants.MessageConstants.ERROR_USER_INVALID_TOKEN;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import com.example.domain.user.model.User;
import com.example.domain.user.service.IUserService;
import com.example.utils.dto.response.ErrorResponseDTO;
import com.example.utils.service.IJwtService;
import com.example.utils.service.IMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/** Filter class for JWT authentication. */
@Log4j2
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final IMessageService messageService;
    private final IJwtService jwtService;
    private final IUserService userService;
    private final ObjectMapper objectMapper;

    /**
     * Filters the request.
     *
     * @param request the HTTP servlet request
     * @param response the HTTP servlet response
     * @param filterChain the filter chain
     * @throws ServletException if an error occurs
     * @throws IOException if an error occurs
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        log.debug("doFilterInternal called");

        if (request.getRequestURI().equals(AUTH_BASE_PATH + LOGIN_PATH)) {
            filterChain.doFilter(request, response);

            return;
        }

        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            setUpInvalidTokenResponse(response);

            return;
        }

        String username = jwtService.getUsernameFromToken(token);

        if (username == null) {
            setUpInvalidTokenResponse(response);

            return;
        }

        User user = userService.getByUsername(username);
        List<SimpleGrantedAuthority> authorities =
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .toList();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, null, authorities);

        authenticationToken.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    /**
     * Sets up the invalid token response.
     *
     * @param response the HTTP servlet response
     * @throws IOException if an error occurs
     */
    private void setUpInvalidTokenResponse(HttpServletResponse response) throws IOException {
        log.debug("invalidTokenResponse called");

        response.setStatus(SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String message =
                messageService.getMessage(ERROR_DEFAULT_MESSAGE)
                        + " "
                        + messageService.getMessage(ERROR_USER_INVALID_TOKEN);
        ErrorResponseDTO errorResponse =
                ErrorResponseDTO.builder()
                        .message(message)
                        .errorCode(HttpServletResponse.SC_UNAUTHORIZED)
                        .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
