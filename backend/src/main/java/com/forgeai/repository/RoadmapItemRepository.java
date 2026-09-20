package com.forgeai.repository;

import com.forgeai.entity.RoadmapItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Data access layer for individual roadmap milestones and items.
@Repository
public interface RoadmapItemRepository extends JpaRepository<RoadmapItem, Long> {

    // Retrieves all items for a given roadmap ordered by phase number.
    List<RoadmapItem> findByRoadmapIdOrderByPhaseNumberAsc(Long roadmapId);
}
