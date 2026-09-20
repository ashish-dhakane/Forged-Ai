package com.forgeai.dto;

// Response payload evaluating a debugging fix submission.
public class DebuggingSubmitResponse {

    private Boolean isSuccessful;
    private Integer attemptNumber;
    private String feedback;
    private Integer xpAwarded;
    private Integer currentXp;
    private Integer currentLevel;
    private String idealSolutionCode;

    public DebuggingSubmitResponse() {}

    public Boolean getIsSuccessful() { return isSuccessful; }
    public void setIsSuccessful(Boolean isSuccessful) { this.isSuccessful = isSuccessful; }

    public Integer getAttemptNumber() { return attemptNumber; }
    public void setAttemptNumber(Integer attemptNumber) { this.attemptNumber = attemptNumber; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public Integer getXpAwarded() { return xpAwarded; }
    public void setXpAwarded(Integer xpAwarded) { this.xpAwarded = xpAwarded; }

    public Integer getCurrentXp() { return currentXp; }
    public void setCurrentXp(Integer currentXp) { this.currentXp = currentXp; }

    public Integer getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(Integer currentLevel) { this.currentLevel = currentLevel; }

    public String getIdealSolutionCode() { return idealSolutionCode; }
    public void setIdealSolutionCode(String idealSolutionCode) { this.idealSolutionCode = idealSolutionCode; }
}
