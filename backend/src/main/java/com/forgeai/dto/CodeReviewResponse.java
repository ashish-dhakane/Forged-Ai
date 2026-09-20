package com.forgeai.dto;

import java.util.List;

// Structured feedback returned by the AI Code Review engine.
public class CodeReviewResponse {

    private Integer qualityScore;
    private String summary;
    private List<String> strengths;
    private List<String> issues;
    private List<String> securityConcerns;
    private List<String> performanceSuggestions;
    private List<String> refactoringSuggestions;
    private String refactoredCode;
    private Boolean isMockAi;

    public CodeReviewResponse() {}

    public Integer getQualityScore() { return qualityScore; }
    public void setQualityScore(Integer qualityScore) { this.qualityScore = qualityScore; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public List<String> getStrengths() { return strengths; }
    public void setStrengths(List<String> strengths) { this.strengths = strengths; }

    public List<String> getIssues() { return issues; }
    public void setIssues(List<String> issues) { this.issues = issues; }

    public List<String> getSecurityConcerns() { return securityConcerns; }
    public void setSecurityConcerns(List<String> securityConcerns) { this.securityConcerns = securityConcerns; }

    public List<String> getPerformanceSuggestions() { return performanceSuggestions; }
    public void setPerformanceSuggestions(List<String> performanceSuggestions) { this.performanceSuggestions = performanceSuggestions; }

    public List<String> getRefactoringSuggestions() { return refactoringSuggestions; }
    public void setRefactoringSuggestions(List<String> refactoringSuggestions) { this.refactoringSuggestions = refactoringSuggestions; }

    public String getRefactoredCode() { return refactoredCode; }
    public void setRefactoredCode(String refactoredCode) { this.refactoredCode = refactoredCode; }

    public Boolean getIsMockAi() { return isMockAi; }
    public void setIsMockAi(Boolean isMockAi) { this.isMockAi = isMockAi; }
}
