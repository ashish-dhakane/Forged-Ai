package com.forgeai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Represents a personalized multi-phase engineering learning path generated for a user.
@Entity
@Table(name = "roadmaps")
public class Roadmap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title = "Personalized Full-Stack & Systems Engineering Roadmap";

    private String targetRole = "Software Engineer (Industry-Ready)";

    private Integer totalPhases = 5;

    private Double overallProgress = 35.0; // 0-100%

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoadmapItem> items = new ArrayList<>();

    public Roadmap() {}

    public Roadmap(User user, String title, String targetRole) {
        this.user = user;
        this.title = title;
        this.targetRole = targetRole;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTargetRole() { return targetRole; }
    public void setTargetRole(String targetRole) { this.targetRole = targetRole; }

    public Integer getTotalPhases() { return totalPhases; }
    public void setTotalPhases(Integer totalPhases) { this.totalPhases = totalPhases; }

    public Double getOverallProgress() { return overallProgress; }
    public void setOverallProgress(Double overallProgress) { this.overallProgress = overallProgress; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<RoadmapItem> getItems() { return items; }
    public void setItems(List<RoadmapItem> items) { this.items = items; }
}
