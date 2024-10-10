package com.example.utils.service;

import com.example.domain.user.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Service class for handling JWT.
 */
@Log4j2
@Service
public class JwtServiceImpl implements IJwtService {
    private final SecretKey SECRET_KEY;
    private final Long EXPIRATION_TIME;

    /**
     * Constructor for JwtServiceImpl.
     *
     * @param secretKey the secret key
     * @param expirationTime the expiration time
     */
    public JwtServiceImpl(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiration}") Long expirationTime) {
        SECRET_KEY = Keys.hmacShaKeyFor(secretKey.getBytes());
        EXPIRATION_TIME = expirationTime;
    }

    /**
     * Generates a jwt token.
     *
     * @param user the user to generate the token for
     * @return the token
     */
    @Override
    public String generateToken(User user) {
        log.debug("generateToken called");

        Map<String, Object> claims = new HashMap<>();

        claims.put("id", user.getId());
        claims.put("username", user.getUsername());

        return createToken(claims, user.getUsername());
    }

    /**
     * Creates a token.
     *
     * @param claims the claims to add to the token
     * @param subject the subject of the token
     * @return the token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        log.debug("createToken called");

        long nowMillis = System.currentTimeMillis();
        long expirationMillis = nowMillis + EXPIRATION_TIME;
        Date now = new Date(nowMillis);
        Date expiration = new Date(expirationMillis);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(SECRET_KEY).compact();
    }
}
