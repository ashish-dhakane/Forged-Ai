package com.forgeai.repository;

import com.forgeai.entity.EngineeringScore;
import com.forgeai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Data access layer for retrieving and persisting user Engineering Scores.
@Repository
public interface EngineeringScoreRepository extends JpaRepository<EngineeringScore, Long> {

    // Retrieves the latest calculated engineering score record for a given user.
    Optional<EngineeringScore> findByUser(User user);

    // Retrieves the latest calculated engineering score record by user ID.
    Optional<EngineeringScore> findByUserId(Long userId);
}
