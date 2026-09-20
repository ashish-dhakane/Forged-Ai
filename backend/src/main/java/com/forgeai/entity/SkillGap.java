package com.forgeai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Stores identified deficits between current competency and target industry benchmark levels.
@Entity
@Table(name = "skill_gaps")
public class SkillGap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String skillName;

    @Column(nullable = false)
    private String category;

    private String currentLevel = "Beginner"; // Beginner, Intermediate, Advanced

    private String targetLevel = "Intermediate"; // Intermediate, Advanced, Expert

    private String gapSeverity = "MEDIUM"; // HIGH, MEDIUM, LOW

    @Column(length = 1000)
    private String recommendedAction;

    private LocalDateTime createdAt = LocalDateTime.now();

    public SkillGap() {}

    public SkillGap(User user, String skillName, String category, String currentLevel, String targetLevel, String gapSeverity, String recommendedAction) {
        this.user = user;
        this.skillName = skillName;
        this.category = category;
        this.currentLevel = currentLevel;
        this.targetLevel = targetLevel;
        this.gapSeverity = gapSeverity;
        this.recommendedAction = recommendedAction;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(String currentLevel) { this.currentLevel = currentLevel; }

    public String getTargetLevel() { return targetLevel; }
    public void setTargetLevel(String targetLevel) { this.targetLevel = targetLevel; }

    public String getGapSeverity() { return gapSeverity; }
    public void setGapSeverity(String gapSeverity) { this.gapSeverity = gapSeverity; }

    public String getRecommendedAction() { return recommendedAction; }
    public void setRecommendedAction(String recommendedAction) { this.recommendedAction = recommendedAction; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
