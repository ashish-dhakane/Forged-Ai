package com.forgeai.dto;

import java.util.Map;

// Summary of assessment score and performance tier.
public class AssessmentResultDto {

    private Long id;
    private Long assessmentId;
    private String assessmentTitle;
    private String category;
    private Double scorePercentage;
    private Integer correctCount;
    private Integer totalQuestions;
    private String performanceTier; // Strong, Average, Needs Improvement
    private Integer xpAwarded;

    public AssessmentResultDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAssessmentId() { return assessmentId; }
    public void setAssessmentId(Long assessmentId) { this.assessmentId = assessmentId; }

    public String getAssessmentTitle() { return assessmentTitle; }
    public void setAssessmentTitle(String assessmentTitle) { this.assessmentTitle = assessmentTitle; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getScorePercentage() { return scorePercentage; }
    public void setScorePercentage(Double scorePercentage) { this.scorePercentage = scorePercentage; }

    public Integer getCorrectCount() { return correctCount; }
    public void setCorrectCount(Integer correctCount) { this.correctCount = correctCount; }

    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }

    public String getPerformanceTier() { return performanceTier; }
    public void setPerformanceTier(String performanceTier) { this.performanceTier = performanceTier; }

    public Integer getXpAwarded() { return xpAwarded; }
    public void setXpAwarded(Integer xpAwarded) { this.xpAwarded = xpAwarded; }
}
