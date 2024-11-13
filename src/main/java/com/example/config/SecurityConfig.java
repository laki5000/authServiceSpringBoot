package com.example.config;

import static com.example.constants.Constants.AUTH_BASE_PATH;
import static com.example.constants.Constants.LOGIN_PATH;

import com.example.domain.user.service.IUserService;
import com.example.security.JwtFilter;
import com.example.utils.service.IJwtService;
import com.example.utils.service.IMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/** Configuration class for Spring Security */
@Log4j2
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * Configures the security filter chain.
     *
     * @param http the HttpSecurity object to configure
     * @param messageService the message service
     * @param jwtService the JWT service
     * @param userService the user service
     * @param objectMapper the object mapper
     * @return the SecurityFilterChain object
     * @throws Exception if an error occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            IMessageService messageService,
            IJwtService jwtService,
            IUserService userService,
            ObjectMapper objectMapper)
            throws Exception {
        log.info("securityFilterChain called");

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers(AUTH_BASE_PATH + LOGIN_PATH)
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated())
                .addFilterBefore(
                        jwtFilter(messageService, jwtService, userService, objectMapper),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Creates a password encoder bean.
     *
     * @return the password encoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("passwordEncoder called");

        return new BCryptPasswordEncoder();
    }

    /**
     * Creates a JWT filter bean.
     *
     * @param messageService the message service
     * @param jwtService the JWT service
     * @param userService the user service
     * @param objectMapper the object mapper
     * @return the JWT filter bean
     */
    @Bean
    public JwtFilter jwtFilter(
            IMessageService messageService,
            IJwtService jwtService,
            IUserService userService,
            ObjectMapper objectMapper) {
        log.info("jwtFilter called");

        return new JwtFilter(messageService, jwtService, userService, objectMapper);
    }
}
