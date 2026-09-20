package com.forgeai.entity;

import jakarta.persistence.*;

// Represents a real-world hands-on engineering assignment designed to close identified skill gaps.
@Entity
@Table(name = "missions")
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private String skillCategory; // Testing, Debugging, Security, System Design, Architecture

    private String difficulty = "Medium"; // Easy, Medium, Hard

    private Integer xpReward = 150;

    @Column(length = 2000)
    private String requirements; // Delimited or JSON string of technical requirements

    @Column(length = 2000)
    private String completionCriteria; // Acceptance criteria

    public Mission() {}

    public Mission(String title, String description, String skillCategory, String difficulty, Integer xpReward, String requirements, String completionCriteria) {
        this.title = title;
        this.description = description;
        this.skillCategory = skillCategory;
        this.difficulty = difficulty;
        this.xpReward = xpReward;
        this.requirements = requirements;
        this.completionCriteria = completionCriteria;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSkillCategory() { return skillCategory; }
    public void setSkillCategory(String skillCategory) { this.skillCategory = skillCategory; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public Integer getXpReward() { return xpReward; }
    public void setXpReward(Integer xpReward) { this.xpReward = xpReward; }

    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }

    public String getCompletionCriteria() { return completionCriteria; }
    public void setCompletionCriteria(String completionCriteria) { this.completionCriteria = completionCriteria; }
}
