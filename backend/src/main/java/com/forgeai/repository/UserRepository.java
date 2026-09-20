package com.forgeai.repository;

import com.forgeai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Data access layer for User entities.
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Looks up a user by their registered email address for authentication.
    Optional<User> findByEmail(String email);

    // Checks if an email is already taken during registration.
    boolean existsByEmail(String email);

    // Finds a user by their associated GitHub handle.
    Optional<User> findByGithubUsername(String githubUsername);
}
