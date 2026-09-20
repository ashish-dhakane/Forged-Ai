package com.forgeai.dto;

import jakarta.validation.constraints.NotBlank;

// Request payload to initiate a new technical interview simulation session.
public class InterviewStartRequest {

    @NotBlank(message = "Target role is required")
    private String role; // Software Engineer, Backend Developer, Frontend Developer, Full Stack Developer

    private String difficulty = "Intermediate"; // Beginner, Intermediate, Advanced

    private String category = "System Design & Coding";

    public InterviewStartRequest() {}

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
