package com.forgeai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

// Represents a concrete milestone item within a personalized engineering roadmap phase.
@Entity
@Table(name = "roadmap_items")
public class RoadmapItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id", nullable = false)
    @JsonIgnore
    private Roadmap roadmap;

    private Integer phaseNumber; // 1 to 5

    @Column(nullable = false)
    private String phaseTitle;

    @Column(nullable = false)
    private String skillName;

    private String currentLevel;

    private String targetLevel;

    @Column(length = 2000)
    private String recommendedResources; // Links or course references

    private Integer estimatedHours = 10;

    private String status = "NOT_STARTED"; // NOT_STARTED, IN_PROGRESS, COMPLETED

    private Integer progressPercentage = 0;

    public RoadmapItem() {}

    public RoadmapItem(Roadmap roadmap, Integer phaseNumber, String phaseTitle, String skillName, String currentLevel, String targetLevel, String recommendedResources, Integer estimatedHours, String status, Integer progressPercentage) {
        this.roadmap = roadmap;
        this.phaseNumber = phaseNumber;
        this.phaseTitle = phaseTitle;
        this.skillName = skillName;
        this.currentLevel = currentLevel;
        this.targetLevel = targetLevel;
        this.recommendedResources = recommendedResources;
        this.estimatedHours = estimatedHours;
        this.status = status;
        this.progressPercentage = progressPercentage;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Roadmap getRoadmap() { return roadmap; }
    public void setRoadmap(Roadmap roadmap) { this.roadmap = roadmap; }

    public Integer getPhaseNumber() { return phaseNumber; }
    public void setPhaseNumber(Integer phaseNumber) { this.phaseNumber = phaseNumber; }

    public String getPhaseTitle() { return phaseTitle; }
    public void setPhaseTitle(String phaseTitle) { this.phaseTitle = phaseTitle; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(String currentLevel) { this.currentLevel = currentLevel; }

    public String getTargetLevel() { return targetLevel; }
    public void setTargetLevel(String targetLevel) { this.targetLevel = targetLevel; }

    public String getRecommendedResources() { return recommendedResources; }
    public void setRecommendedResources(String recommendedResources) { this.recommendedResources = recommendedResources; }

    public Integer getEstimatedHours() { return estimatedHours; }
    public void setEstimatedHours(Integer estimatedHours) { this.estimatedHours = estimatedHours; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Integer progressPercentage) { this.progressPercentage = progressPercentage; }
}
