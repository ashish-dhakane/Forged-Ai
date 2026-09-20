package com.forgeai.service;

import com.forgeai.dto.MissionSubmitRequest;
import com.forgeai.entity.Mission;
import com.forgeai.entity.MissionSubmission;
import com.forgeai.entity.User;
import com.forgeai.exception.ResourceNotFoundException;
import com.forgeai.repository.MissionRepository;
import com.forgeai.repository.MissionSubmissionRepository;
import com.forgeai.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// Manages real-world hands-on engineering missions, XP allocation, and level advancements.
@Service
public class MissionService {

    private final MissionRepository missionRepository;
    private final MissionSubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final ScoreCalculationService scoreCalculationService;

    public MissionService(MissionRepository missionRepository, MissionSubmissionRepository submissionRepository,
                          UserRepository userRepository, ScoreCalculationService scoreCalculationService) {
        this.missionRepository = missionRepository;
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
        this.scoreCalculationService = scoreCalculationService;
    }

    // Fetches all available engineering missions in the platform.
    public List<Mission> getAllMissions() {
        return missionRepository.findAll();
    }

    // Fetches user submissions and completion progress for all missions.
    public List<MissionSubmission> getUserSubmissions(Long userId) {
        return submissionRepository.findByUserId(userId);
    }

    // Submits or marks an engineering mission as completed, awarding XP and recalculating level.
    @Transactional
    public MissionSubmission submitMission(Long userId, Long missionId, MissionSubmitRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new ResourceNotFoundException("Mission not found: " + missionId));

        MissionSubmission submission = submissionRepository.findByUserIdAndMissionId(userId, missionId)
                .orElse(new MissionSubmission(user, mission, "IN_PROGRESS", request.getRepositoryUrl(), request.getSubmissionNotes()));

        submission.setStatus("COMPLETED");
        submission.setRepositoryUrl(request.getRepositoryUrl());
        submission.setSubmissionNotes(request.getSubmissionNotes());
        submission.setCompletedAt(LocalDateTime.now());

        MissionSubmission savedSubmission = submissionRepository.save(submission);

        // Awards XP reward and calculates level (1 level per 500 XP)
        int newXp = (user.getXp() != null ? user.getXp() : 0) + mission.getXpReward();
        user.setXp(newXp);
        user.setLevel(Math.max(1, (newXp / 500) + 1));
        userRepository.save(user);

        // Triggers recalculation of the user's Engineering Score
        scoreCalculationService.calculateAndSaveScores(userId);

        return savedSubmission;
    }
}
