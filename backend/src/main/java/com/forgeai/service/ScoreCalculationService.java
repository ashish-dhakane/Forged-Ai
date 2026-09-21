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
    private final ProjectRepository projectRepository;
    private final InterviewRepository interviewRepository;
    private final CodeReviewRepository codeReviewRepository;
    private final SkillGapService skillGapService;

    public ScoreCalculationService(
            EngineeringScoreRepository scoreRepository,
            UserRepository userRepository,
            AssessmentResultRepository assessmentResultRepository,
            DebuggingAttemptRepository debuggingAttemptRepository,
            MissionSubmissionRepository missionSubmissionRepository,
            RepositoryEntityRepository repositoryEntityRepository,
            ProjectRepository projectRepository,
            InterviewRepository interviewRepository,
            CodeReviewRepository codeReviewRepository,
            SkillGapService skillGapService) {
        this.scoreRepository = scoreRepository;
        this.userRepository = userRepository;
        this.assessmentResultRepository = assessmentResultRepository;
        this.debuggingAttemptRepository = debuggingAttemptRepository;
        this.missionSubmissionRepository = missionSubmissionRepository;
        this.repositoryEntityRepository = repositoryEntityRepository;
        this.projectRepository = projectRepository;
        this.interviewRepository = interviewRepository;
        this.codeReviewRepository = codeReviewRepository;
        this.skillGapService = skillGapService;
    }

    // Calculates and persists the weighted engineering score and industry readiness pillars for a user.
    @Transactional
    public EngineeringScore calculateAndSaveScores(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        EngineeringScore score = scoreRepository.findByUserId(userId)
                .orElse(new EngineeringScore());
        score.setUser(user);

        boolean isDemoUser = "demo@forgeai.dev".equalsIgnoreCase(user.getEmail());

        double programming;
        double problemSolving;
        double debugging;
        double testing;
        double systemDesign;
        double security;
        double communication;
        double github;
        double projects;
        double consistency;

        if (isDemoUser) {
            // Seeded baseline benchmarks for viva demonstration mode
            programming = calculateCategoryScore(userId, "Programming", 75.0);
            problemSolving = calculateCategoryScore(userId, "Problem Solving", 72.0);
            debugging = calculateDebuggingScore(user, 62.0, true);
            testing = calculateTestingScore(userId, 58.0, true);
            systemDesign = calculateCategoryScore(userId, "System Design", 60.0);
            security = calculateCategoryScore(userId, "Security", 65.0);
            communication = calculateCategoryScore(userId, "Communication", 75.0);
            github = calculateGithubScore(userId, 68.0, true);
            projects = calculateProjectsScore(userId, 70.0, true);
            consistency = calculateConsistencyScore(user, 80.0, true);
        } else {
            // Real deterministic calculation powered by actual database records
            programming = calculateRealProgramming(userId);
            problemSolving = calculateRealProblemSolving(user);
            debugging = calculateDebuggingScore(user, 0.0, false);
            testing = calculateTestingScore(userId, 0.0, false);
            systemDesign = calculateRealSystemDesign(userId);
            security = calculateRealSecurity(userId);
            communication = calculateRealCommunication(userId);
            github = calculateGithubScore(userId, 0.0, false);
            projects = calculateProjectsScore(userId, 0.0, false);
            consistency = calculateConsistencyScore(user, 0.0, false);
        }

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

        // Identifies the focus area to formulate actionable guidance
        if (overallScore == 0.0) {
            score.setCurrentFocus("Kickstart Portfolio & Assessments");
            score.setFocusReason("Take your first skill assessment, solve an interactive debugging challenge, or sync your GitHub repository to generate your initial Engineering Score.");
        } else if (testing < 65.0 && debugging < 65.0) {
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
            score.setFocusReason("Strong foundation across competencies. Focus on high-throughput architecture and complex production debugging.");
        }

        score.setCalculatedAt(LocalDateTime.now());
        EngineeringScore saved = scoreRepository.save(score);

        // Dynamically recalculate skill gaps based on latest score
        if (skillGapService != null) {
            try {
                skillGapService.recalculateSkillGaps(userId);
            } catch (Exception ignored) {}
        }

        return saved;
    }

    // Fetches the user's score DTO for dashboard presentation.
    public ScoreDto getUserScoreDto(Long userId) {
        EngineeringScore entity = scoreRepository.findByUserId(userId)
                .orElseGet(() -> calculateAndSaveScores(userId));

        User user = entity.getUser() != null ? entity.getUser() : userRepository.findById(userId).orElse(null);
        boolean isDemoUser = user != null && "demo@forgeai.dev".equalsIgnoreCase(user.getEmail());

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
        dto.setIsDemoData(isDemoUser);
        return dto;
    }

    private double calculateRealProgramming(Long userId) {
        List<AssessmentResult> results = assessmentResultRepository.findByUserIdOrderByCompletedAtDesc(userId);
        double progAssessment = results.stream()
                .filter(r -> r.getCategory() != null && r.getCategory().toLowerCase().contains("programming"))
                .mapToDouble(AssessmentResult::getScorePercentage)
                .average().orElse(-1.0);

        List<CodeReview> reviews = codeReviewRepository.findByUserIdOrderByCreatedAtDesc(userId);
        double reviewScore = reviews.stream()
                .filter(r -> r.getQualityScore() != null)
                .mapToDouble(CodeReview::getQualityScore)
                .average().orElse(-1.0);

        if (progAssessment >= 0 && reviewScore >= 0) {
            return (progAssessment * 0.7) + (reviewScore * 0.3);
        } else if (progAssessment >= 0) {
            return progAssessment;
        } else if (reviewScore >= 0) {
            return reviewScore;
        }
        return 0.0;
    }

    private double calculateRealProblemSolving(User user) {
        Long userId = user.getId();
        List<AssessmentResult> results = assessmentResultRepository.findByUserIdOrderByCompletedAtDesc(userId);
        double psAssessment = results.stream()
                .filter(r -> r.getCategory() != null && (r.getCategory().toLowerCase().contains("problem") || r.getCategory().toLowerCase().contains("algorithm")))
                .mapToDouble(AssessmentResult::getScorePercentage)
                .average().orElse(-1.0);

        List<DebuggingAttempt> attempts = debuggingAttemptRepository.findByUser(user);
        long successfulCount = attempts.stream().filter(DebuggingAttempt::getIsSuccessful).count();
        double debugBonus = Math.min(50.0, successfulCount * 15.0);

        if (psAssessment >= 0) {
            return Math.min(100.0, (psAssessment * 0.7) + (debugBonus * 0.6));
        } else if (debugBonus > 0) {
            return Math.min(100.0, debugBonus * 2.0);
        }
        return 0.0;
    }

    private double calculateRealSystemDesign(Long userId) {
        List<AssessmentResult> results = assessmentResultRepository.findByUserIdOrderByCompletedAtDesc(userId);
        double sysAssessment = results.stream()
                .filter(r -> r.getCategory() != null && (r.getCategory().toLowerCase().contains("system") || r.getCategory().toLowerCase().contains("architect")))
                .mapToDouble(AssessmentResult::getScorePercentage)
                .average().orElse(-1.0);

        List<Interview> interviews = interviewRepository.findByUserIdOrderByCreatedAtDesc(userId);
        double interviewTech = interviews.stream()
                .filter(i -> "COMPLETED".equalsIgnoreCase(i.getStatus()) && i.getTechnicalScore() != null)
                .mapToDouble(Interview::getTechnicalScore)
                .average().orElse(-1.0);

        if (sysAssessment >= 0 && interviewTech >= 0) {
            return (sysAssessment * 0.6) + (interviewTech * 0.4);
        } else if (sysAssessment >= 0) {
            return sysAssessment;
        } else if (interviewTech >= 0) {
            return interviewTech;
        }
        return 0.0;
    }

    private double calculateRealSecurity(Long userId) {
        List<AssessmentResult> results = assessmentResultRepository.findByUserIdOrderByCompletedAtDesc(userId);
        double secAssessment = results.stream()
                .filter(r -> r.getCategory() != null && r.getCategory().toLowerCase().contains("security"))
                .mapToDouble(AssessmentResult::getScorePercentage)
                .average().orElse(-1.0);

        List<MissionSubmission> submissions = missionSubmissionRepository.findByUserId(userId);
        long secMissions = submissions.stream()
                .filter(s -> "COMPLETED".equalsIgnoreCase(s.getStatus()) &&
                        s.getMission() != null && s.getMission().getSkillCategory() != null &&
                        s.getMission().getSkillCategory().toLowerCase().contains("security"))
                .count();

        if (secAssessment >= 0) {
            return Math.min(100.0, secAssessment + (secMissions * 10.0));
        } else if (secMissions > 0) {
            return Math.min(100.0, secMissions * 35.0);
        }
        return 0.0;
    }

    private double calculateRealCommunication(Long userId) {
        List<AssessmentResult> results = assessmentResultRepository.findByUserIdOrderByCompletedAtDesc(userId);
        double commAssessment = results.stream()
                .filter(r -> r.getCategory() != null && r.getCategory().toLowerCase().contains("communication"))
                .mapToDouble(AssessmentResult::getScorePercentage)
                .average().orElse(-1.0);

        List<Interview> interviews = interviewRepository.findByUserIdOrderByCreatedAtDesc(userId);
        double interviewComm = interviews.stream()
                .filter(i -> "COMPLETED".equalsIgnoreCase(i.getStatus()) && i.getCommunicationScore() != null)
                .mapToDouble(Interview::getCommunicationScore)
                .average().orElse(-1.0);

        if (commAssessment >= 0 && interviewComm >= 0) {
            return (commAssessment * 0.4) + (interviewComm * 0.6);
        } else if (interviewComm >= 0) {
            return interviewComm;
        } else if (commAssessment >= 0) {
            return commAssessment;
        }
        return 0.0;
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
    private double calculateDebuggingScore(User user, double defaultBase, boolean isDemo) {
        List<DebuggingAttempt> attempts = debuggingAttemptRepository.findByUser(user);
        if (attempts.isEmpty()) return defaultBase;
        long successful = attempts.stream().filter(DebuggingAttempt::getIsSuccessful).count();
        double ratio = (double) successful / Math.max(attempts.size(), 1);
        if (isDemo) {
            return Math.min(100.0, defaultBase + (ratio * 25.0));
        } else {
            return Math.min(100.0, (ratio * 60.0) + Math.min(successful * 10.0, 40.0));
        }
    }

    // Adjusts testing score based on submitted testing missions and assessments.
    private double calculateTestingScore(Long userId, double defaultBase, boolean isDemo) {
        List<MissionSubmission> submissions = missionSubmissionRepository.findByUserId(userId);
        long testingMissions = submissions.stream()
                .filter(s -> "COMPLETED".equalsIgnoreCase(s.getStatus()) &&
                        s.getMission() != null && s.getMission().getSkillCategory() != null &&
                        "Testing".equalsIgnoreCase(s.getMission().getSkillCategory()))
                .count();

        List<AssessmentResult> results = assessmentResultRepository.findByUserIdOrderByCompletedAtDesc(userId);
        double testAssessment = results.stream()
                .filter(r -> r.getCategory() != null && "Testing".equalsIgnoreCase(r.getCategory()))
                .mapToDouble(AssessmentResult::getScorePercentage)
                .average().orElse(-1.0);

        if (isDemo) {
            return Math.min(100.0, defaultBase + (testingMissions * 10.0));
        } else {
            if (testAssessment >= 0) {
                return Math.min(100.0, testAssessment + (testingMissions * 10.0));
            } else if (testingMissions > 0) {
                return Math.min(100.0, testingMissions * 35.0);
            }
            return 0.0;
        }
    }

    // Calculates GitHub contribution and code health score from synced repositories.
    private double calculateGithubScore(Long userId, double defaultBase, boolean isDemo) {
        List<RepositoryEntity> repos = repositoryEntityRepository.findByUserId(userId);
        if (repos.isEmpty()) return defaultBase;
        double avgHealth = repos.stream().mapToDouble(RepositoryEntity::getHealthScore).average().orElse(defaultBase);
        if (isDemo) {
            return Math.min(100.0, (avgHealth * 0.7) + (Math.min(repos.size() * 5.0, 30.0)));
        } else {
            return Math.min(100.0, (avgHealth * 0.7) + (Math.min(repos.size() * 5.0, 30.0)));
        }
    }

    // Calculates projects portfolio complexity score.
    private double calculateProjectsScore(Long userId, double defaultBase, boolean isDemo) {
        List<Project> projects = projectRepository.findByUserId(userId);
        List<RepositoryEntity> repos = repositoryEntityRepository.findByUserId(userId);
        if (isDemo) {
            return (projects.size() + repos.size()) >= 3 ? Math.min(100.0, defaultBase + 10.0) : defaultBase;
        } else {
            if (projects.isEmpty() && repos.isEmpty()) return 0.0;
            return Math.min(100.0, (projects.size() * 35.0) + (repos.size() * 15.0));
        }
    }

    // Calculates consistency score based on user streak.
    private double calculateConsistencyScore(User user, double defaultBase, boolean isDemo) {
        if (isDemo) {
            if (user == null || user.getStreak() == null) return defaultBase;
            return Math.min(100.0, 50.0 + (user.getStreak() * 5.0));
        } else {
            if (user == null || user.getStreak() == null || user.getStreak() <= 0) return 0.0;
            return Math.min(100.0, 30.0 + (user.getStreak() * 10.0));
        }
    }

    // Utility helper to round scores to one decimal place for UI readability.
    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}

