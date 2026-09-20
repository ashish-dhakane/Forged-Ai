package com.forgeai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Stores the structured AI code analysis and refactoring recommendations for a student's code snippet.
@Entity
@Table(name = "code_reviews")
public class CodeReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String language; // Java, TypeScript, Python, Go, C++

    @Column(nullable = false, length = 10000)
    private String codeSnippet;

    private Integer qualityScore = 75; // 0-100

    @Column(length = 2000)
    private String summary;

    @Column(length = 4000)
    private String strengthsJson;

    @Column(length = 4000)
    private String issuesJson;

    @Column(length = 4000)
    private String securityConcernsJson;

    @Column(length = 4000)
    private String performanceSuggestionsJson;

    @Column(length = 10000)
    private String refactoredCode;

    private LocalDateTime createdAt = LocalDateTime.now();

    public CodeReview() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getCodeSnippet() { return codeSnippet; }
    public void setCodeSnippet(String codeSnippet) { this.codeSnippet = codeSnippet; }

    public Integer getQualityScore() { return qualityScore; }
    public void setQualityScore(Integer qualityScore) { this.qualityScore = qualityScore; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getStrengthsJson() { return strengthsJson; }
    public void setStrengthsJson(String strengthsJson) { this.strengthsJson = strengthsJson; }

    public String getIssuesJson() { return issuesJson; }
    public void setIssuesJson(String issuesJson) { this.issuesJson = issuesJson; }

    public String getSecurityConcernsJson() { return securityConcernsJson; }
    public void setSecurityConcernsJson(String securityConcernsJson) { this.securityConcernsJson = securityConcernsJson; }

    public String getPerformanceSuggestionsJson() { return performanceSuggestionsJson; }
    public void setPerformanceSuggestionsJson(String performanceSuggestionsJson) { this.performanceSuggestionsJson = performanceSuggestionsJson; }

    public String getRefactoredCode() { return refactoredCode; }
    public void setRefactoredCode(String refactoredCode) { this.refactoredCode = refactoredCode; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
