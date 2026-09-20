package com.forgeai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Stores GitHub repository metadata, health metrics, and automated code health insights.
@Entity
@Table(name = "repositories")
public class RepositoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    private String fullName;

    @Column(length = 2000)
    private String description;

    private String language;

    private Integer starsCount = 0;

    private Integer forksCount = 0;

    private Integer openIssuesCount = 0;

    private String htmlUrl;

    private Double healthScore = 78.0;

    @Column(length = 2000)
    private String detectedTechnologies;

    @Column(length = 2000)
    private String strengthsJson;

    @Column(length = 2000)
    private String improvementsJson;

    private LocalDateTime lastSyncedAt = LocalDateTime.now();

    public RepositoryEntity() {}

    public RepositoryEntity(User user, String name, String fullName, String description, String language, Integer starsCount, Integer forksCount, String htmlUrl, Double healthScore, String detectedTechnologies, String strengthsJson, String improvementsJson) {
        this.user = user;
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.language = language;
        this.starsCount = starsCount;
        this.forksCount = forksCount;
        this.htmlUrl = htmlUrl;
        this.healthScore = healthScore;
        this.detectedTechnologies = detectedTechnologies;
        this.strengthsJson = strengthsJson;
        this.improvementsJson = improvementsJson;
        this.lastSyncedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public Integer getStarsCount() { return starsCount; }
    public void setStarsCount(Integer starsCount) { this.starsCount = starsCount; }

    public Integer getForksCount() { return forksCount; }
    public void setForksCount(Integer forksCount) { this.forksCount = forksCount; }

    public Integer getOpenIssuesCount() { return openIssuesCount; }
    public void setOpenIssuesCount(Integer openIssuesCount) { this.openIssuesCount = openIssuesCount; }

    public String getHtmlUrl() { return htmlUrl; }
    public void setHtmlUrl(String htmlUrl) { this.htmlUrl = htmlUrl; }

    public Double getHealthScore() { return healthScore; }
    public void setHealthScore(Double healthScore) { this.healthScore = healthScore; }

    public String getDetectedTechnologies() { return detectedTechnologies; }
    public void setDetectedTechnologies(String detectedTechnologies) { this.detectedTechnologies = detectedTechnologies; }

    public String getStrengthsJson() { return strengthsJson; }
    public void setStrengthsJson(String strengthsJson) { this.strengthsJson = strengthsJson; }

    public String getImprovementsJson() { return improvementsJson; }
    public void setImprovementsJson(String improvementsJson) { this.improvementsJson = improvementsJson; }

    public LocalDateTime getLastSyncedAt() { return lastSyncedAt; }
    public void setLastSyncedAt(LocalDateTime lastSyncedAt) { this.lastSyncedAt = lastSyncedAt; }
}
