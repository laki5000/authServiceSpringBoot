package com.example.domain.user.model;

import static jakarta.persistence.GenerationType.IDENTITY;

import com.example.base.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/** Entity class for user roles. */
@Entity
@Table(name = "roles")
@Getter
public class Role extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String name;
}
