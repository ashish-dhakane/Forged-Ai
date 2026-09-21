package com.forgeai.service;

import com.forgeai.entity.EngineeringScore;
import com.forgeai.entity.SkillGap;
import com.forgeai.entity.User;
import com.forgeai.repository.EngineeringScoreRepository;
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
    private final EngineeringScoreRepository scoreRepository;

    public SkillGapService(SkillGapRepository skillGapRepository,
                           UserRepository userRepository,
                           EngineeringScoreRepository scoreRepository) {
        this.skillGapRepository = skillGapRepository;
        this.userRepository = userRepository;
        this.scoreRepository = scoreRepository;
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

    // Re-evaluates skill gaps dynamically based on latest persisted Engineering Score dimensions.
    @Transactional
    public List<SkillGap> recalculateSkillGaps(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        // Retrieve current persisted score metrics
        EngineeringScore score = scoreRepository.findByUserId(userId).orElse(null);
        boolean isDemoUser = "demo@forgeai.dev".equalsIgnoreCase(user.getEmail());

        double testingScore = score != null ? (score.getTestingScore() != null ? score.getTestingScore() : 0.0) : (isDemoUser ? 58.0 : 0.0);
        double debuggingScore = score != null ? (score.getDebuggingScore() != null ? score.getDebuggingScore() : 0.0) : (isDemoUser ? 62.0 : 0.0);
        double sysDesignScore = score != null ? (score.getSystemDesignScore() != null ? score.getSystemDesignScore() : 0.0) : (isDemoUser ? 60.0 : 0.0);
        double securityScore = score != null ? (score.getSecurityScore() != null ? score.getSecurityScore() : 0.0) : (isDemoUser ? 65.0 : 0.0);
        double probSolvingScore = score != null ? (score.getProblemSolvingScore() != null ? score.getProblemSolvingScore() : 0.0) : (isDemoUser ? 72.0 : 0.0);
        double githubScore = score != null ? (score.getGithubScore() != null ? score.getGithubScore() : 0.0) : (isDemoUser ? 68.0 : 0.0);

        // Delete outdated gap records for fresh recalculation
        List<SkillGap> existingGaps = skillGapRepository.findByUserId(userId);
        skillGapRepository.deleteAll(existingGaps);

        List<SkillGap> newGaps = new ArrayList<>();

        // 1. Testing Skill Gap
        newGaps.add(createGap(
                user,
                "Automated Unit & Integration Testing",
                "Testing",
                testingScore,
                "Implement JUnit 5 unit tests and Mockito integration tests in your current Spring Boot backend.",
                "Expand test suite with parameterized boundary checks and integration slices.",
                "Maintain comprehensive regression test suites and automated continuous integration checks."
        ));

        // 2. Debugging Skill Gap
        newGaps.add(createGap(
                user,
                "Defect Root-Cause Analysis & Profiling",
                "Debugging",
                debuggingScore,
                "Practice root-cause analysis on interactive debugging puzzles across Python, TypeScript, and Java.",
                "Practice concurrency bug isolation, deadlock detection, and memory profiling.",
                "Conduct deep architectural profiling and production observability diagnostics."
        ));

        // 3. System Design Skill Gap
        newGaps.add(createGap(
                user,
                "Distributed System Design & Caching",
                "System Design",
                sysDesignScore,
                "Study distributed caching patterns, database indexing, and REST architectural constraints.",
                "Incorporate Redis caching layers and message queuing for asynchronous long-running tasks.",
                "Architect multi-region fault tolerance, sharding strategies, and high-throughput pipelines."
        ));

        // 4. Security Skill Gap
        newGaps.add(createGap(
                user,
                "Application Security & OWASP Defense",
                "Security",
                securityScore,
                "Audit API endpoints against SQL injection, XSS, and enforce strict CORS with rate limiting.",
                "Harden stateless JWT token lifecycles, sanitize user inputs, and mitigate OWASP Top 10 vulnerabilities.",
                "Integrate automated static security analysis and vulnerability scanning in CI pipelines."
        ));

        // 5. Problem Solving Skill Gap
        newGaps.add(createGap(
                user,
                "Algorithmic Optimization & Complexity Analysis",
                "Problem Solving",
                probSolvingScore,
                "Complete technical assessments to solidify core data structures and algorithmic fundamentals.",
                "Solve graph traversal and dynamic programming challenges with rigorous space/time proofs.",
                "Optimize critical path operations with low-latency algorithms and cache-friendly structures."
        ));

        // 6. GitHub & Code Health Gap
        newGaps.add(createGap(
                user,
                "Repository Architecture & Open Source Cadence",
                "GitHub",
                githubScore,
                "Synchronize active public GitHub repositories and establish consistent commit history.",
                "Improve repository health scores with documentation, issue templates, and CI/CD pipelines.",
                "Maintain high-impact open-source contributions with robust multi-language code health."
        ));

        return skillGapRepository.saveAll(newGaps);
    }

    // Helper method to compute level, severity, and contextual action from dynamic score thresholds
    private SkillGap createGap(User user, String skillName, String category, double score,
                               String beginnerAction, String intermediateAction, String advancedAction) {
        String currentLevel;
        String targetLevel;
        String severity;
        String recommendedAction;

        if (score < 50.0) {
            currentLevel = "Beginner";
            targetLevel = "Intermediate";
            severity = "HIGH";
            recommendedAction = beginnerAction;
        } else if (score < 75.0) {
            currentLevel = "Intermediate";
            targetLevel = "Advanced";
            severity = "MEDIUM";
            recommendedAction = intermediateAction;
        } else {
            currentLevel = "Advanced";
            targetLevel = "Mastery";
            severity = "LOW";
            recommendedAction = advancedAction;
        }

        return new SkillGap(user, skillName, category, currentLevel, targetLevel, severity, recommendedAction);
    }
}
