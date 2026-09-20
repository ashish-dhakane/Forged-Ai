package com.forgeai.repository;

import com.forgeai.entity.Roadmap;
import com.forgeai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Data access layer for user personalized learning roadmaps.
@Repository
public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {

    // Retrieves the active personalized roadmap for a user.
    Optional<Roadmap> findByUser(User user);

    // Retrieves the active personalized roadmap by user ID.
    Optional<Roadmap> findByUserId(Long userId);
}
