package com.example.utils.service;

import static com.example.constants.MessageConstants.ERROR_TOKEN_DECRYPTION;
import static com.example.constants.MessageConstants.ERROR_TOKEN_ENCRYPTION;

import com.example.domain.invalidatedToken.service.IInvalidatedTokenService;
import com.example.domain.user.model.Role;
import com.example.domain.user.model.User;
import com.example.exception.TokenEncryptionException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
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
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration}") Long expirationTime)
            throws NoSuchPaddingException, NoSuchAlgorithmException {
        String algorithm = "AES";

        this.messageService = messageService;
        this.invalidatedTokenService = invalidatedTokenService;
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

        return encryptToken(
                Jwts.builder()
                        .claims(claims)
                        .subject(subject)
                        .issuedAt(now)
                        .expiration(expiration)
                        .signWith(SECRET_KEY)
                        .compact());
    }

    /**
     * Encrypts a token.
     *
     * @param token the token to encrypt
     * @return the encrypted token
     * @throws TokenEncryptionException if the token cannot be encrypted
     */
    private String encryptToken(String token) {
        log.debug("encryptToken called");

        try {
            CIPHER.init(Cipher.ENCRYPT_MODE, SECRET_KEY_SPEC);

            return Base64.getEncoder().encodeToString(CIPHER.doFinal(token.getBytes()));
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new TokenEncryptionException(
                    messageService.getMessage(ERROR_TOKEN_ENCRYPTION), e);
        }
    }

    /**
     * Decrypts a token.
     *
     * @param token the token to decrypt
     * @return the decrypted token
     * @throws TokenEncryptionException if the token cannot be decrypted
     */
    private String decryptToken(String token) {
        log.debug("decryptToken called");

        try {
            CIPHER.init(Cipher.DECRYPT_MODE, SECRET_KEY_SPEC);

            return new String(CIPHER.doFinal(Base64.getDecoder().decode(token)));
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new TokenEncryptionException(
                    messageService.getMessage(ERROR_TOKEN_DECRYPTION), e);
        }
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

        String decryptedToken = decryptToken(token);

        invalidatedTokenService.create(token, getExpireAt(decryptedToken));
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
}
