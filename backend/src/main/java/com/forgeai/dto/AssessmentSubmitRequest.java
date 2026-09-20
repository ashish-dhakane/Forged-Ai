package com.forgeai.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

// Request payload containing the user's answers to an assessment.
public class AssessmentSubmitRequest {

    @NotNull(message = "Assessment ID is required")
    private Long assessmentId;

    // Map of question ID to chosen option index (0-indexed)
    @NotNull(message = "Answers map is required")
    private Map<Long, Integer> answers;

    public AssessmentSubmitRequest() {}

    public AssessmentSubmitRequest(Long assessmentId, Map<Long, Integer> answers) {
        this.assessmentId = assessmentId;
        this.answers = answers;
    }

    public Long getAssessmentId() { return assessmentId; }
    public void setAssessmentId(Long assessmentId) { this.assessmentId = assessmentId; }

    public Map<Long, Integer> getAnswers() { return answers; }
    public void setAnswers(Map<Long, Integer> answers) { this.answers = answers; }
}
