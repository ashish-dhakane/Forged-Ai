package com.forgeai.service;

import com.forgeai.dto.ScoreDto;
import com.forgeai.entity.*;
import com.forgeai.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// Deterministically computes the user's transparent 10-dimension Engineering Score and Industry Readiness metrics.
@Service
public class ScoreCalculationService {

    // Defined deterministic scoring weights matching platform specifications
    public static final double WEIGHT_PROGRAMMING = 0.20;
    public static final double WEIGHT_PROBLEM_SOLVING = 0.15;
    public static final double WEIGHT_PROJECTS = 0.15;
    public static final double WEIGHT_GITHUB = 0.10;
    public static final double WEIGHT_DEBUGGING = 0.10;
    public static final double WEIGHT_TESTING = 0.08;
    public static final double WEIGHT_SYSTEM_DESIGN = 0.08;
    public static final double WEIGHT_SECURITY = 0.05;
    public static final double WEIGHT_COMMUNICATION = 0.05;
    public static final double WEIGHT_CONSISTENCY = 0.04;

    private final EngineeringScoreRepository scoreRepository;
    private final UserRepository userRepository;
    private final AssessmentResultRepository assessmentResultRepository;
    private final DebuggingAttemptRepository debuggingAttemptRepository;
    private final MissionSubmissionRepository missionSubmissionRepository;
    private final RepositoryEntityRepository repositoryEntityRepository;

    public ScoreCalculationService(
            EngineeringScoreRepository scoreRepository,
            UserRepository userRepository,
            AssessmentResultRepository assessmentResultRepository,
            DebuggingAttemptRepository debuggingAttemptRepository,
            MissionSubmissionRepository missionSubmissionRepository,
            RepositoryEntityRepository repositoryEntityRepository) {
        this.scoreRepository = scoreRepository;
        this.userRepository = userRepository;
        this.assessmentResultRepository = assessmentResultRepository;
        this.debuggingAttemptRepository = debuggingAttemptRepository;
        this.missionSubmissionRepository = missionSubmissionRepository;
        this.repositoryEntityRepository = repositoryEntityRepository;
    }

    // Calculates and persists the weighted engineering score and industry readiness pillars for a user.
    @Transactional
    public EngineeringScore calculateAndSaveScores(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        EngineeringScore score = scoreRepository.findByUserId(userId)
                .orElse(new EngineeringScore());
        score.setUser(user);

        // Calculate dynamic dimensions based on actual user activity or baseline calibrated benchmarks
        double programming = calculateCategoryScore(userId, "Programming", 75.0);
        double problemSolving = calculateCategoryScore(userId, "Problem Solving", 72.0);
        double debugging = calculateDebuggingScore(userId, 62.0);
        double testing = calculateTestingScore(userId, 58.0);
        double systemDesign = calculateCategoryScore(userId, "System Design", 60.0);
        double security = calculateCategoryScore(userId, "Security", 65.0);
        double communication = calculateCategoryScore(userId, "Communication", 75.0);
        double github = calculateGithubScore(userId, 68.0);
        double projects = calculateProjectsScore(userId, 70.0);
        double consistency = calculateConsistencyScore(user, 80.0);

        score.setProgrammingScore(round(programming));
        score.setProblemSolvingScore(round(problemSolving));
        score.setDebuggingScore(round(debugging));
        score.setTestingScore(round(testing));
        score.setSystemDesignScore(round(systemDesign));
        score.setSecurityScore(round(security));
        score.setCommunicationScore(round(communication));
        score.setGithubScore(round(github));
        score.setProjectsScore(round(projects));
        score.setConsistencyScore(round(consistency));

        // Deterministic Composite Engineering Score calculation (sum of weights = 1.00)
        double overallScore = (programming * WEIGHT_PROGRAMMING)
                + (problemSolving * WEIGHT_PROBLEM_SOLVING)
                + (projects * WEIGHT_PROJECTS)
                + (github * WEIGHT_GITHUB)
                + (debugging * WEIGHT_DEBUGGING)
                + (testing * WEIGHT_TESTING)
                + (systemDesign * WEIGHT_SYSTEM_DESIGN)
                + (security * WEIGHT_SECURITY)
                + (communication * WEIGHT_COMMUNICATION)
                + (consistency * WEIGHT_CONSISTENCY);

        score.setOverallScore(round(overallScore));

        // Deterministic Industry Readiness Score calculation across 4 professional pillars
        double technicalReadiness = (programming * 0.40) + (problemSolving * 0.30) + (debugging * 0.15) + (testing * 0.15);
        double projectReadiness = (projects * 0.60) + (github * 0.40);
        double interviewReadiness = (communication * 0.40) + (systemDesign * 0.30) + (problemSolving * 0.30);
        double engineeringPracticeReadiness = (testing * 0.35) + (security * 0.35) + (debugging * 0.30);

        double industryReadiness = (technicalReadiness * 0.35)
                + (projectReadiness * 0.25)
                + (engineeringPracticeReadiness * 0.20)
                + (interviewReadiness * 0.20);

        score.setTechnicalReadiness(round(technicalReadiness));
        score.setProjectReadiness(round(projectReadiness));
        score.setInterviewReadiness(round(interviewReadiness));
        score.setEngineeringPracticeReadiness(round(engineeringPracticeReadiness));
        score.setIndustryReadinessScore(round(industryReadiness));

        // Identifies the lowest scoring area to formulate actionable guidance
        if (testing < 65.0 && debugging < 65.0) {
            score.setCurrentFocus("Debugging & Testing");
            score.setFocusReason("Your project activity suggests more practice is needed in debugging and automated test suites.");
        } else if (systemDesign < 65.0) {
            score.setCurrentFocus("System Design");
            score.setFocusReason("Solid programming fundamentals detected; prioritize distributed system design and API contracts.");
        } else if (security < 65.0) {
            score.setCurrentFocus("Application Security");
            score.setFocusReason("Improve input sanitization, JWT authorization flows, and vulnerability defense.");
        } else {
            score.setCurrentFocus("Full-Stack Systems Mastery");
            score.setFocusReason("Strong foundation across all competencies. Focus on high-throughput architecture and complex debugging.");
        }

        score.setCalculatedAt(LocalDateTime.now());
        return scoreRepository.save(score);
    }

    // Fetches the user's score DTO for dashboard presentation.
    public ScoreDto getUserScoreDto(Long userId) {
        EngineeringScore entity = scoreRepository.findByUserId(userId)
                .orElseGet(() -> calculateAndSaveScores(userId));

        ScoreDto dto = new ScoreDto();
        dto.setOverallScore(entity.getOverallScore());
        dto.setIndustryReadinessScore(entity.getIndustryReadinessScore());
        dto.setProgramming(entity.getProgrammingScore());
        dto.setProblemSolving(entity.getProblemSolvingScore());
        dto.setProjects(entity.getProjectsScore());
        dto.setGithub(entity.getGithubScore());
        dto.setDebugging(entity.getDebuggingScore());
        dto.setTesting(entity.getTestingScore());
        dto.setSystemDesign(entity.getSystemDesignScore());
        dto.setSecurity(entity.getSecurityScore());
        dto.setCommunication(entity.getCommunicationScore());
        dto.setConsistency(entity.getConsistencyScore());
        dto.setTechnicalReadiness(entity.getTechnicalReadiness());
        dto.setProjectReadiness(entity.getProjectReadiness());
        dto.setInterviewReadiness(entity.getInterviewReadiness());
        dto.setEngineeringPracticeReadiness(entity.getEngineeringPracticeReadiness());
        dto.setCurrentFocus(entity.getCurrentFocus());
        dto.setFocusReason(entity.getFocusReason());
        dto.setIsDemoData(userId.equals(1L) || (entity.getUser() != null && "demo@forgeai.dev".equals(entity.getUser().getEmail())));
        return dto;
    }

    // Calculates category score from completed assessments with fallback to calibrated base.
    private double calculateCategoryScore(Long userId, String category, double defaultBase) {
        List<AssessmentResult> results = assessmentResultRepository.findByUserIdOrderByCompletedAtDesc(userId);
        return results.stream()
                .filter(r -> category.equalsIgnoreCase(r.getCategory()))
                .mapToDouble(AssessmentResult::getScorePercentage)
                .average()
                .orElse(defaultBase);
    }

    // Adjusts debugging score based on successful puzzle attempts.
    private double calculateDebuggingScore(Long userId, double defaultBase) {
        List<DebuggingAttempt> attempts = debuggingAttemptRepository.findByUser(
                userRepository.findById(userId).orElse(null)
        );
        if (attempts.isEmpty()) return defaultBase;
        long successful = attempts.stream().filter(DebuggingAttempt::getIsSuccessful).count();
        double ratio = (double) successful / Math.max(attempts.size(), 1);
        return Math.min(100.0, defaultBase + (ratio * 25.0));
    }

    // Adjusts testing score based on submitted testing missions and assessments.
    private double calculateTestingScore(Long userId, double defaultBase) {
        List<MissionSubmission> submissions = missionSubmissionRepository.findByUserId(userId);
        long testingMissions = submissions.stream()
                .filter(s -> "COMPLETED".equals(s.getStatus()) &&
                        s.getMission() != null &&
                        "Testing".equalsIgnoreCase(s.getMission().getSkillCategory()))
                .count();
        return Math.min(100.0, defaultBase + (testingMissions * 10.0));
    }

    // Calculates GitHub contribution and code health score from synced repositories.
    private double calculateGithubScore(Long userId, double defaultBase) {
        List<RepositoryEntity> repos = repositoryEntityRepository.findByUserId(userId);
        if (repos.isEmpty()) return defaultBase;
        double avgHealth = repos.stream().mapToDouble(RepositoryEntity::getHealthScore).average().orElse(defaultBase);
        return Math.min(100.0, (avgHealth * 0.7) + (Math.min(repos.size() * 5.0, 30.0)));
    }

    // Calculates projects portfolio complexity score.
    private double calculateProjectsScore(Long userId, double defaultBase) {
        List<RepositoryEntity> repos = repositoryEntityRepository.findByUserId(userId);
        return repos.size() >= 3 ? Math.min(100.0, defaultBase + 10.0) : defaultBase;
    }

    // Calculates consistency score based on user streak.
    private double calculateConsistencyScore(User user, double defaultBase) {
        if (user == null || user.getStreak() == null) return defaultBase;
        return Math.min(100.0, 50.0 + (user.getStreak() * 5.0));
    }

    // Utility helper to round scores to one decimal place for UI readability.
    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
