package com.forgeai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Records a user's submission, progress, and verification status for an engineering mission.
@Entity
@Table(name = "mission_submissions")
public class MissionSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    private String status = "IN_PROGRESS"; // IN_PROGRESS, COMPLETED

    private String repositoryUrl;

    @Column(length = 2000)
    private String submissionNotes;

    private LocalDateTime submittedAt = LocalDateTime.now();

    private LocalDateTime completedAt;

    public MissionSubmission() {}

    public MissionSubmission(User user, Mission mission, String status, String repositoryUrl, String submissionNotes) {
        this.user = user;
        this.mission = mission;
        this.status = status;
        this.repositoryUrl = repositoryUrl;
        this.submissionNotes = submissionNotes;
        this.submittedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Mission getMission() { return mission; }
    public void setMission(Mission mission) { this.mission = mission; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRepositoryUrl() { return repositoryUrl; }
    public void setRepositoryUrl(String repositoryUrl) { this.repositoryUrl = repositoryUrl; }

    public String getSubmissionNotes() { return submissionNotes; }
    public void setSubmissionNotes(String submissionNotes) { this.submissionNotes = submissionNotes; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
