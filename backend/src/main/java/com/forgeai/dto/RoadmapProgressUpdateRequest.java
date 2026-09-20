package com.forgeai.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Request payload to update the progress and status of a roadmap learning milestone.
public class RoadmapProgressUpdateRequest {

    @NotBlank(message = "Status is required (NOT_STARTED, IN_PROGRESS, COMPLETED)")
    private String status;

    @NotNull(message = "Progress percentage is required")
    @Min(0)
    @Max(100)
    private Integer progressPercentage;

    public RoadmapProgressUpdateRequest() {}

    public RoadmapProgressUpdateRequest(String status, Integer progressPercentage) {
        this.status = status;
        this.progressPercentage = progressPercentage;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Integer progressPercentage) { this.progressPercentage = progressPercentage; }
}
