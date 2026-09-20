package com.forgeai.repository;

import com.forgeai.entity.Mission;
import com.forgeai.entity.MissionSubmission;
import com.forgeai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

// Data access layer for user mission submissions.
@Repository
public interface MissionSubmissionRepository extends JpaRepository<MissionSubmission, Long> {

    // Retrieves all mission submissions by a specific user.
    List<MissionSubmission> findByUser(User user);

    // Retrieves all mission submissions for a given user ID.
    List<MissionSubmission> findByUserId(Long userId);

    // Finds a submission for a specific user and mission.
    Optional<MissionSubmission> findByUserAndMission(User user, Mission mission);

    // Finds a submission for a specific user ID and mission ID.
    Optional<MissionSubmission> findByUserIdAndMissionId(Long userId, Long missionId);
}
