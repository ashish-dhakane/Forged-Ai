package com.forgeai.entity;

import jakarta.persistence.*;

// Represents an interactive defective code puzzle designed to teach systematic debugging.
@Entity
@Table(name = "debugging_challenges")
public class DebuggingChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private String language; // Java, TypeScript, Python

    private String difficulty = "Medium"; // Easy, Medium, Hard

    @Column(nullable = false, length = 5000)
    private String buggyCode;

    @Column(length = 5000)
    private String solutionCode;

    @Column(length = 2000)
    private String bugExplanation;

    @Column(length = 2000)
    private String hintsJson; // e.g. ["Look at the loop termination index", "Notice race condition"]

    @Column(length = 2000)
    private String testCaseDescription;

    private Integer xpReward = 100;

    public DebuggingChallenge() {}

    public DebuggingChallenge(String title, String description, String language, String difficulty, String buggyCode, String solutionCode, String bugExplanation, String hintsJson, String testCaseDescription, Integer xpReward) {
        this.title = title;
        this.description = description;
        this.language = language;
        this.difficulty = difficulty;
        this.buggyCode = buggyCode;
        this.solutionCode = solutionCode;
        this.bugExplanation = bugExplanation;
        this.hintsJson = hintsJson;
        this.testCaseDescription = testCaseDescription;
        this.xpReward = xpReward;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getBuggyCode() { return buggyCode; }
    public void setBuggyCode(String buggyCode) { this.buggyCode = buggyCode; }

    public String getSolutionCode() { return solutionCode; }
    public void setSolutionCode(String solutionCode) { this.solutionCode = solutionCode; }

    public String getBugExplanation() { return bugExplanation; }
    public void setBugExplanation(String bugExplanation) { this.bugExplanation = bugExplanation; }

    public String getHintsJson() { return hintsJson; }
    public void setHintsJson(String hintsJson) { this.hintsJson = hintsJson; }

    public String getTestCaseDescription() { return testCaseDescription; }
    public void setTestCaseDescription(String testCaseDescription) { this.testCaseDescription = testCaseDescription; }

    public Integer getXpReward() { return xpReward; }
    public void setXpReward(Integer xpReward) { this.xpReward = xpReward; }
}
