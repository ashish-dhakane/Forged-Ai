package com.forgeai.repository;

import com.forgeai.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Data access layer for engineering missions.
@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {

    // Retrieves missions by target skill category.
    List<Mission> findBySkillCategory(String skillCategory);

    // Retrieves missions by difficulty tier.
    List<Mission> findByDifficulty(String difficulty);
}
