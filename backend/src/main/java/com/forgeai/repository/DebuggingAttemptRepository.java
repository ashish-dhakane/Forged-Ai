package com.forgeai.repository;

import com.forgeai.entity.DebuggingAttempt;
import com.forgeai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Data access layer for student debugging challenge attempts.
@Repository
public interface DebuggingAttemptRepository extends JpaRepository<DebuggingAttempt, Long> {

    // Retrieves all attempts made by a user.
    List<DebuggingAttempt> findByUser(User user);

    // Retrieves all attempts made for a specific debugging challenge.
    List<DebuggingAttempt> findByUserIdAndChallengeId(Long userId, Long challengeId);
}
