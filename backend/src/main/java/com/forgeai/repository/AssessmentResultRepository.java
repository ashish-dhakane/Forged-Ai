package com.forgeai.repository;

import com.forgeai.entity.AssessmentResult;
import com.forgeai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Data access layer for submitted assessment outcomes.
@Repository
public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Long> {

    // Retrieves all assessment results for a specific user.
    List<AssessmentResult> findByUser(User user);

    // Retrieves all assessment results for a user ID ordered by most recent.
    List<AssessmentResult> findByUserIdOrderByCompletedAtDesc(Long userId);
}
