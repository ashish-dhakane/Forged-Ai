package com.forgeai.repository;

import com.forgeai.entity.SkillGap;
import com.forgeai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Data access layer for detected user skill gaps.
@Repository
public interface SkillGapRepository extends JpaRepository<SkillGap, Long> {

    // Retrieves all identified skill gaps for a user.
    List<SkillGap> findByUser(User user);

    // Retrieves all identified skill gaps for a given user ID.
    List<SkillGap> findByUserId(Long userId);

    // Retrieves skill gaps filtered by severity (HIGH, MEDIUM, LOW).
    List<SkillGap> findByUserIdAndGapSeverity(Long userId, String gapSeverity);
}
