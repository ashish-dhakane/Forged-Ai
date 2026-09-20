package com.forgeai.repository;

import com.forgeai.entity.Interview;
import com.forgeai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Data access layer for AI technical interview sessions.
@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    // Retrieves all interview sessions conducted for a user.
    List<Interview> findByUser(User user);

    // Retrieves interview sessions for a user ID ordered by newest first.
    List<Interview> findByUserIdOrderByCreatedAtDesc(Long userId);
}
