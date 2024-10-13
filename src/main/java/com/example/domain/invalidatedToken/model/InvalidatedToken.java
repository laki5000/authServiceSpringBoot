package com.example.domain.invalidatedToken.model;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

/** Entity class for invalidated tokens. */
@Entity
@Table(name = "invalidated_tokens")
@Getter
@Setter
@Builder
public class InvalidatedToken {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String token;

    @NotNull
    @Column(nullable = false, updatable = false)
    private Instant expiresAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
