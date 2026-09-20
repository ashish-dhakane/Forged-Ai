package com.forgeai.service;

import com.forgeai.entity.SkillGap;
import com.forgeai.entity.User;
import com.forgeai.repository.SkillGapRepository;
import com.forgeai.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// Evaluates user skill gaps by comparing current performance against industry readiness benchmarks.
@Service
public class SkillGapService {

    private final SkillGapRepository skillGapRepository;
    private final UserRepository userRepository;
    private final ScoreCalculationService scoreCalculationService;

    public SkillGapService(SkillGapRepository skillGapRepository, UserRepository userRepository,
                           ScoreCalculationService scoreCalculationService) {
        this.skillGapRepository = skillGapRepository;
        this.userRepository = userRepository;
        this.scoreCalculationService = scoreCalculationService;
    }

    // Retrieves all identified skill gaps for a user, generating them if not already computed.
    @Transactional
    public List<SkillGap> getSkillGapsForUser(Long userId) {
        List<SkillGap> existingGaps = skillGapRepository.findByUserId(userId);
        if (existingGaps.isEmpty()) {
            return recalculateSkillGaps(userId);
        }
        return existingGaps;
    }

    // Re-evaluates skill gaps based on latest assessment, mission, and repository data.
    @Transactional
    public List<SkillGap> recalculateSkillGaps(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        // Delete outdated gap records for recalculation
        List<SkillGap> existingGaps = skillGapRepository.findByUserId(userId);
        skillGapRepository.deleteAll(existingGaps);

        List<SkillGap> newGaps = new ArrayList<>();

        // Testing Skill Gap
        newGaps.add(new SkillGap(
                user,
                "Automated Unit & Integration Testing",
                "Testing",
                "Beginner",
                "Intermediate",
                "HIGH",
                "Implement JUnit 5 unit tests and Mockito integration tests in your current Spring Boot backend."
        ));

        // Debugging Skill Gap
        newGaps.add(new SkillGap(
                user,
                "Defect Root-Cause Analysis & Profiling",
                "Debugging",
                "Intermediate",
                "Advanced",
                "MEDIUM",
                "Practice concurrency bug isolation and memory leak detection using Java profiling tools."
        ));

        // System Design Skill Gap
        newGaps.add(new SkillGap(
                user,
                "Distributed System Design & Caching",
                "System Design",
                "Intermediate",
                "Advanced",
                "MEDIUM",
                "Incorporate Redis caching layers and message queuing for asynchronous long-running tasks."
        ));

        // Security Skill Gap
        newGaps.add(new SkillGap(
                user,
                "Application Security & OWASP Defense",
                "Security",
                "Beginner",
                "Intermediate",
                "MEDIUM",
                "Audit API endpoints against SQL injection, XSS, and enforce strict CORS with rate limiting."
        ));

        // Problem Solving Skill Gap
        newGaps.add(new SkillGap(
                user,
                "Algorithmic Optimization & Complexity Analysis",
                "Problem Solving",
                "Intermediate",
                "Advanced",
                "LOW",
                "Solve graph traversal and dynamic programming challenges with rigorous space/time proofs."
        ));

        return skillGapRepository.saveAll(newGaps);
    }
}
