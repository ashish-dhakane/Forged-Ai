package com.forgeai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Stores the outcome and evaluated score of a user's completed skill assessment.
@Entity
@Table(name = "assessment_results")
public class AssessmentResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    private Double scorePercentage;

    private Integer totalQuestions;

    private Integer correctCount;

    private String category;

    private String performanceTier; // Strong, Average, Needs Improvement

    private LocalDateTime completedAt = LocalDateTime.now();

    public AssessmentResult() {}

    public AssessmentResult(User user, Assessment assessment, Double scorePercentage, Integer totalQuestions, Integer correctCount, String category, String performanceTier) {
        this.user = user;
        this.assessment = assessment;
        this.scorePercentage = scorePercentage;
        this.totalQuestions = totalQuestions;
        this.correctCount = correctCount;
        this.category = category;
        this.performanceTier = performanceTier;
        this.completedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Assessment getAssessment() { return assessment; }
    public void setAssessment(Assessment assessment) { this.assessment = assessment; }

    public Double getScorePercentage() { return scorePercentage; }
    public void setScorePercentage(Double scorePercentage) { this.scorePercentage = scorePercentage; }

    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }

    public Integer getCorrectCount() { return correctCount; }
    public void setCorrectCount(Integer correctCount) { this.correctCount = correctCount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPerformanceTier() { return performanceTier; }
    public void setPerformanceTier(String performanceTier) { this.performanceTier = performanceTier; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
