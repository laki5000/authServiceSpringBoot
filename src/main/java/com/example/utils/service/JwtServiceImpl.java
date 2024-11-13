package com.example.utils.service;

import static com.example.constants.Constants.ERROR_TOKEN_ENCRYPTION;

import com.example.domain.invalidatedToken.service.IInvalidatedTokenService;
import com.example.domain.user.model.Role;
import com.example.domain.user.model.User;
import com.example.exception.TokenEncryptionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** Service class for handling JWT. */
@Log4j2
@Service
public class JwtServiceImpl implements IJwtService {
    private final IMessageService messageService;
    private final IInvalidatedTokenService invalidatedTokenService;
    private final ObjectMapper objectMapper;
    private final SecretKey SECRET_KEY;
    private final SecretKeySpec SECRET_KEY_SPEC;
    private final Cipher CIPHER;
    private final Long EXPIRATION_TIME;

    /**
     * Constructor for JwtServiceImpl.
     *
     * @param secretKey the secret key
     * @param expirationTime the expiration time
     * @throws NoSuchPaddingException if the padding is invalid
     * @throws NoSuchAlgorithmException if the algorithm is invalid
     */
    public JwtServiceImpl(
            IMessageService messageService,
            IInvalidatedTokenService invalidatedTokenService,
            ObjectMapper objectMapper,
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration}") Long expirationTime)
            throws NoSuchPaddingException, NoSuchAlgorithmException {
        String algorithm = "AES";

        this.messageService = messageService;
        this.invalidatedTokenService = invalidatedTokenService;
        this.objectMapper = objectMapper;
        this.SECRET_KEY = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.SECRET_KEY_SPEC = new SecretKeySpec(secretKey.getBytes(), algorithm);
        this.CIPHER = Cipher.getInstance(algorithm);
        this.EXPIRATION_TIME = expirationTime;
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
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("roles", roles);

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
                .claim("encryptedClaims", encryptClaims(claims))
                .subject(subject)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * Encrypts the claims of a token.
     *
     * @param claims the claims to encrypt
     * @return the encrypted claims
     * @throws TokenEncryptionException if the claims cannot be encrypted
     */
    private String encryptClaims(Map<String, Object> claims) {
        log.debug("encryptClaims called");

        String encryptedClaims = null;

        try {
            String claimsString = objectMapper.writeValueAsString(claims);

            CIPHER.init(Cipher.ENCRYPT_MODE, SECRET_KEY_SPEC);

            encryptedClaims =
                    Base64.getEncoder().encodeToString(CIPHER.doFinal(claimsString.getBytes()));
        } catch (JsonProcessingException
                | InvalidKeyException
                | IllegalBlockSizeException
                | BadPaddingException e) {
            throw new TokenEncryptionException(
                    messageService.getMessage(ERROR_TOKEN_ENCRYPTION), e);
        }

        return encryptedClaims;
    }

    /**
     * Invalidates a token.
     *
     * @param token the token to invalidate
     */
    @Override
    public void invalidateToken(String token) {
        log.debug("invalidateToken called");

        token = token.substring(7);

        invalidatedTokenService.create(token, getExpireAt(token));
    }

    /**
     * Gets the expiration time of a token.
     *
     * @param token the token
     * @return the expiration time
     */
    private Instant getExpireAt(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .toInstant();
    }

    /**
     * Gets the username from a token.
     *
     * @param token the token
     * @return the username
     */
    @Override
    public String getUsernameFromToken(String token) {
        log.debug("getUsernameFromToken called");

        token = token.substring(7);

        if (invalidatedTokenService.existsByToken(token)) {
            return null;
        }

        try {
            return Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (ExpiredJwtException | SignatureException | IllegalArgumentException e) {
            return null;
        }
    }
}
