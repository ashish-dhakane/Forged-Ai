package com.forgeai.dto;

import java.util.Map;

// Data transfer object representing the breakdown of Engineering and Industry Readiness scores.
public class ScoreDto {

    private Double overallScore;
    private Double industryReadinessScore;

    // 10 Weighted Dimensions
    private Double programming;
    private Double problemSolving;
    private Double projects;
    private Double github;
    private Double debugging;
    private Double testing;
    private Double systemDesign;
    private Double security;
    private Double communication;
    private Double consistency;

    // Readiness Components
    private Double technicalReadiness;
    private Double projectReadiness;
    private Double interviewReadiness;
    private Double engineeringPracticeReadiness;

    private String currentFocus;
    private String focusReason;
    private Boolean isDemoData;

    public ScoreDto() {}

    public Double getOverallScore() { return overallScore; }
    public void setOverallScore(Double overallScore) { this.overallScore = overallScore; }

    public Double getIndustryReadinessScore() { return industryReadinessScore; }
    public void setIndustryReadinessScore(Double industryReadinessScore) { this.industryReadinessScore = industryReadinessScore; }

    public Double getProgramming() { return programming; }
    public void setProgramming(Double programming) { this.programming = programming; }

    public Double getProblemSolving() { return problemSolving; }
    public void setProblemSolving(Double problemSolving) { this.problemSolving = problemSolving; }

    public Double getProjects() { return projects; }
    public void setProjects(Double projects) { this.projects = projects; }

    public Double getGithub() { return github; }
    public void setGithub(Double github) { this.github = github; }

    public Double getDebugging() { return debugging; }
    public void setDebugging(Double debugging) { this.debugging = debugging; }

    public Double getTesting() { return testing; }
    public void setTesting(Double testing) { this.testing = testing; }

    public Double getSystemDesign() { return systemDesign; }
    public void setSystemDesign(Double systemDesign) { this.systemDesign = systemDesign; }

    public Double getSecurity() { return security; }
    public void setSecurity(Double security) { this.security = security; }

    public Double getCommunication() { return communication; }
    public void setCommunication(Double communication) { this.communication = communication; }

    public Double getConsistency() { return consistency; }
    public void setConsistency(Double consistency) { this.consistency = consistency; }

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

    public Boolean getIsDemoData() { return isDemoData; }
    public void setIsDemoData(Boolean isDemoData) { this.isDemoData = isDemoData; }
}
