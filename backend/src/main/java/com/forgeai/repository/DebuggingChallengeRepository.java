package com.forgeai.repository;

import com.forgeai.entity.DebuggingChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Data access layer for debugging challenges.
@Repository
public interface DebuggingChallengeRepository extends JpaRepository<DebuggingChallenge, Long> {

    // Retrieves debugging challenges matching a specific programming language.
    List<DebuggingChallenge> findByLanguage(String language);

    // Retrieves debugging challenges matching a difficulty level.
    List<DebuggingChallenge> findByDifficulty(String difficulty);
}
