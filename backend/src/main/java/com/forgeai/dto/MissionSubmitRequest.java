package com.forgeai.dto;

import jakarta.validation.constraints.NotBlank;

// Request payload for submitting an engineering mission verification.
public class MissionSubmitRequest {

    private String repositoryUrl;

    @NotBlank(message = "Please provide notes or a summary of your implementation")
    private String submissionNotes;

    public MissionSubmitRequest() {}

    public MissionSubmitRequest(String repositoryUrl, String submissionNotes) {
        this.repositoryUrl = repositoryUrl;
        this.submissionNotes = submissionNotes;
    }

    public String getRepositoryUrl() { return repositoryUrl; }
    public void setRepositoryUrl(String repositoryUrl) { this.repositoryUrl = repositoryUrl; }

    public String getSubmissionNotes() { return submissionNotes; }
    public void setSubmissionNotes(String submissionNotes) { this.submissionNotes = submissionNotes; }
}
