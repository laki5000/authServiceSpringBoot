package com.example.domain.user.repository;

import com.example.domain.user.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/** Repository interface for user-related operations. */
@Repository
public interface IUserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    /**
     * Finds a user by username.
     *
     * @param username the username
     * @return the user
     */
    Optional<User> findByUsername(String username);
}
