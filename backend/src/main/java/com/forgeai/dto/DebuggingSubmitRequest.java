package com.forgeai.dto;

import jakarta.validation.constraints.NotBlank;

// Request payload submitted when solving a debugging challenge.
public class DebuggingSubmitRequest {

    @NotBlank(message = "Please describe what was causing the bug")
    private String userExplanation;

    @NotBlank(message = "Fixed code cannot be empty")
    private String userFixedCode;

    public DebuggingSubmitRequest() {}

    public DebuggingSubmitRequest(String userExplanation, String userFixedCode) {
        this.userExplanation = userExplanation;
        this.userFixedCode = userFixedCode;
    }

    public String getUserExplanation() { return userExplanation; }
    public void setUserExplanation(String userExplanation) { this.userExplanation = userExplanation; }

    public String getUserFixedCode() { return userFixedCode; }
    public void setUserFixedCode(String userFixedCode) { this.userFixedCode = userFixedCode; }
}
