package com.forgeai.dto;

import java.util.List;

// Evaluated interview performance metrics and hiring recommendation.
public class InterviewEvaluationResponse {

    private Long interviewId;
    private Double overallScore;
    private Double technicalScore;
    private Double communicationScore;
    private Double problemSolvingScore;
    private String summary;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> improvementSuggestions;
    private String readinessVerdict; // Ready, Near Ready, Needs Practice

    public InterviewEvaluationResponse() {}

    public Long getInterviewId() { return interviewId; }
    public void setInterviewId(Long interviewId) { this.interviewId = interviewId; }

    public Double getOverallScore() { return overallScore; }
    public void setOverallScore(Double overallScore) { this.overallScore = overallScore; }

    public Double getTechnicalScore() { return technicalScore; }
    public void setTechnicalScore(Double technicalScore) { this.technicalScore = technicalScore; }

    public Double getCommunicationScore() { return communicationScore; }
    public void setCommunicationScore(Double communicationScore) { this.communicationScore = communicationScore; }

    public Double getProblemSolvingScore() { return problemSolvingScore; }
    public void setProblemSolvingScore(Double problemSolvingScore) { this.problemSolvingScore = problemSolvingScore; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public List<String> getStrengths() { return strengths; }
    public void setStrengths(List<String> strengths) { this.strengths = strengths; }

    public List<String> getWeaknesses() { return weaknesses; }
    public void setWeaknesses(List<String> weaknesses) { this.weaknesses = weaknesses; }

    public List<String> getImprovementSuggestions() { return improvementSuggestions; }
    public void setImprovementSuggestions(List<String> improvementSuggestions) { this.improvementSuggestions = improvementSuggestions; }

    public String getReadinessVerdict() { return readinessVerdict; }
    public void setReadinessVerdict(String readinessVerdict) { this.readinessVerdict = readinessVerdict; }
}
