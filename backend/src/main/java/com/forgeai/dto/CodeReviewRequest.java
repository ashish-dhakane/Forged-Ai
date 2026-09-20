package com.forgeai.dto;

import jakarta.validation.constraints.NotBlank;

// Request payload containing user code submitted for AI analysis.
public class CodeReviewRequest {

    @NotBlank(message = "Code snippet cannot be empty")
    private String codeSnippet;

    @NotBlank(message = "Programming language is required")
    private String language;

    public CodeReviewRequest() {}

    public CodeReviewRequest(String codeSnippet, String language) {
        this.codeSnippet = codeSnippet;
        this.language = language;
    }

    public String getCodeSnippet() { return codeSnippet; }
    public void setCodeSnippet(String codeSnippet) { this.codeSnippet = codeSnippet; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}
