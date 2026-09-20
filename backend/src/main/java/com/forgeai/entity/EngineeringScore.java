package com.forgeai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Stores the user's deterministically calculated Engineering Score and Industry Readiness metrics.
@Entity
@Table(name = "engineering_scores")
public class EngineeringScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Overall composite engineering score (0-100)
    private Double overallScore = 70.0;

    // Industry readiness score (0-100)
    private Double industryReadinessScore = 65.0;

    // 10 Weighted Category Dimensions
    private Double programmingScore = 75.0;     // 20%
    private Double problemSolvingScore = 70.0;   // 15%
    private Double projectsScore = 70.0;         // 15%
    private Double githubScore = 65.0;           // 10%
    private Double debuggingScore = 60.0;        // 10%
    private Double testingScore = 55.0;          // 8%
    private Double systemDesignScore = 60.0;     // 8%
    private Double securityScore = 65.0;         // 5%
    private Double communicationScore = 75.0;    // 5%
    private Double consistencyScore = 80.0;      // 4%

    // Industry Readiness Sub-Scores
    private Double technicalReadiness = 72.0;
    private Double projectReadiness = 68.0;
    private Double interviewReadiness = 62.0;
    private Double engineeringPracticeReadiness = 58.0;

    // Current Focus recommendation
    private String currentFocus = "Debugging & Testing";
    private String focusReason = "Your project activity and assessment results suggest more practice is needed in unit testing and defect isolation.";

    private LocalDateTime calculatedAt = LocalDateTime.now();

    public EngineeringScore() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Double getOverallScore() { return overallScore; }
    public void setOverallScore(Double overallScore) { this.overallScore = overallScore; }

    public Double getIndustryReadinessScore() { return industryReadinessScore; }
    public void setIndustryReadinessScore(Double industryReadinessScore) { this.industryReadinessScore = industryReadinessScore; }

    public Double getProgrammingScore() { return programmingScore; }
    public void setProgrammingScore(Double programmingScore) { this.programmingScore = programmingScore; }

    public Double getProblemSolvingScore() { return problemSolvingScore; }
    public void setProblemSolvingScore(Double problemSolvingScore) { this.problemSolvingScore = problemSolvingScore; }

    public Double getProjectsScore() { return projectsScore; }
    public void setProjectsScore(Double projectsScore) { this.projectsScore = projectsScore; }

    public Double getGithubScore() { return githubScore; }
    public void setGithubScore(Double githubScore) { this.githubScore = githubScore; }

    public Double getDebuggingScore() { return debuggingScore; }
    public void setDebuggingScore(Double debuggingScore) { this.debuggingScore = debuggingScore; }

    public Double getTestingScore() { return testingScore; }
    public void setTestingScore(Double testingScore) { this.testingScore = testingScore; }

    public Double getSystemDesignScore() { return systemDesignScore; }
    public void setSystemDesignScore(Double systemDesignScore) { this.systemDesignScore = systemDesignScore; }

    public Double getSecurityScore() { return securityScore; }
    public void setSecurityScore(Double securityScore) { this.securityScore = securityScore; }

    public Double getCommunicationScore() { return communicationScore; }
    public void setCommunicationScore(Double communicationScore) { this.communicationScore = communicationScore; }

    public Double getConsistencyScore() { return consistencyScore; }
    public void setConsistencyScore(Double consistencyScore) { this.consistencyScore = consistencyScore; }

    public Double getTechnicalReadiness() { return technicalReadiness; }
    public void setTechnicalReadiness(Double technicalReadiness) { this.technicalReadiness = technicalReadiness; }

    public Double getProjectReadiness() { return projectReadiness; }
    public void setProjectReadiness(Double projectReadiness) { this.projectReadiness = projectReadiness; }

    public Double getInterviewReadiness() { return interviewReadiness; }
    public void setInterviewReadiness(Double interviewReadiness) { this.interviewReadiness = interviewReadiness; }

    public Double getEngineeringPracticeReadiness() { return engineeringPracticeReadiness; }
    public void setEngineeringPracticeReadiness(Double engineeringPracticeReadiness) { this.engineeringPracticeReadiness = engineeringPracticeReadiness; }

    public String getCurrentFocus() { return currentFocus; }
    public void setCurrentFocus(String currentFocus) { this.currentFocus = currentFocus; }

    public String getFocusReason() { return focusReason; }
    public void setFocusReason(String focusReason) { this.focusReason = focusReason; }

    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; }
}
