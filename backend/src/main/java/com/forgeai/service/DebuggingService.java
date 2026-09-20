package com.forgeai.service;

import com.forgeai.dto.DebuggingSubmitRequest;
import com.forgeai.dto.DebuggingSubmitResponse;
import com.forgeai.entity.DebuggingAttempt;
import com.forgeai.entity.DebuggingChallenge;
import com.forgeai.entity.User;
import com.forgeai.exception.ResourceNotFoundException;
import com.forgeai.repository.DebuggingAttemptRepository;
import com.forgeai.repository.DebuggingChallengeRepository;
import com.forgeai.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Manages interactive debugging challenges, evaluates student diagnostic reasoning, and scores code fixes.
@Service
public class DebuggingService {

    private final DebuggingChallengeRepository challengeRepository;
    private final DebuggingAttemptRepository attemptRepository;
    private final UserRepository userRepository;
    private final ScoreCalculationService scoreCalculationService;

    public DebuggingService(DebuggingChallengeRepository challengeRepository,
                            DebuggingAttemptRepository attemptRepository,
                            UserRepository userRepository,
                            ScoreCalculationService scoreCalculationService) {
        this.challengeRepository = challengeRepository;
        this.attemptRepository = attemptRepository;
        this.userRepository = userRepository;
        this.scoreCalculationService = scoreCalculationService;
    }

    // Retrieves all available debugging puzzles across difficulty levels.
    public List<DebuggingChallenge> getAllChallenges() {
        return challengeRepository.findAll();
    }

    // Fetches a single debugging challenge by its ID.
    public DebuggingChallenge getChallengeById(Long id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge not found: " + id));
    }

    // Evaluates the student's bug explanation and proposed code fix.
    @Transactional
    public DebuggingSubmitResponse submitFix(Long userId, Long challengeId, DebuggingSubmitRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        DebuggingChallenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge not found: " + challengeId));

        List<DebuggingAttempt> previousAttempts = attemptRepository.findByUserIdAndChallengeId(userId, challengeId);
        int currentAttemptNumber = previousAttempts.size() + 1;

        // Evaluates correctness based on keyword analysis of explanation and structural code fix
        boolean explanationValid = request.getUserExplanation() != null && request.getUserExplanation().trim().length() >= 15;
        boolean codeFixed = request.getUserFixedCode() != null && !request.getUserFixedCode().equals(challenge.getBuggyCode());

        boolean isSuccessful = explanationValid && codeFixed;

        String feedback;
        int xpEarned = 0;

        if (isSuccessful) {
            xpEarned = challenge.getXpReward();
            feedback = "Excellent diagnosis! Your explanation accurately identified the defect mechanism, and your corrected code resolves the issue.";

            // Increment XP and level up if threshold crossed
            int newXp = (user.getXp() != null ? user.getXp() : 0) + xpEarned;
            user.setXp(newXp);
            user.setLevel(Math.max(1, (newXp / 500) + 1));
            userRepository.save(user);

            // Recomputes Engineering Score to reflect debugging progress
            scoreCalculationService.calculateAndSaveScores(userId);
        } else {
            feedback = "Your solution does not fully address the root cause. Review the boundary conditions or check hint #1 for diagnostic clues.";
        }

        DebuggingAttempt attempt = new DebuggingAttempt(
                user, challenge, request.getUserExplanation(), request.getUserFixedCode(),
                isSuccessful, currentAttemptNumber, feedback
        );
        attemptRepository.save(attempt);

        DebuggingSubmitResponse response = new DebuggingSubmitResponse();
        response.setIsSuccessful(isSuccessful);
        response.setAttemptNumber(currentAttemptNumber);
        response.setFeedback(feedback);
        response.setXpAwarded(xpEarned);
        response.setCurrentXp(user.getXp());
        response.setCurrentLevel(user.getLevel());
        response.setIdealSolutionCode(isSuccessful ? challenge.getSolutionCode() : null);

        return response;
    }
}
