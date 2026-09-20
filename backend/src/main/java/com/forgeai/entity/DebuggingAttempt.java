package com.forgeai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Tracks each attempt made by a user to diagnose and resolve a debugging challenge.
@Entity
@Table(name = "debugging_attempts")
public class DebuggingAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private DebuggingChallenge challenge;

    @Column(length = 2000)
    private String userExplanation;

    @Column(length = 5000)
    private String userFixedCode;

    private Boolean isSuccessful = false;

    private Integer attemptNumber = 1;

    @Column(length = 2000)
    private String feedback;

    private LocalDateTime createdAt = LocalDateTime.now();

    public DebuggingAttempt() {}

    public DebuggingAttempt(User user, DebuggingChallenge challenge, String userExplanation, String userFixedCode, Boolean isSuccessful, Integer attemptNumber, String feedback) {
        this.user = user;
        this.challenge = challenge;
        this.userExplanation = userExplanation;
        this.userFixedCode = userFixedCode;
        this.isSuccessful = isSuccessful;
        this.attemptNumber = attemptNumber;
        this.feedback = feedback;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public DebuggingChallenge getChallenge() { return challenge; }
    public void setChallenge(DebuggingChallenge challenge) { this.challenge = challenge; }

    public String getUserExplanation() { return userExplanation; }
    public void setUserExplanation(String userExplanation) { this.userExplanation = userExplanation; }

    public String getUserFixedCode() { return userFixedCode; }
    public void setUserFixedCode(String userFixedCode) { this.userFixedCode = userFixedCode; }

    public Boolean getIsSuccessful() { return isSuccessful; }
    public void setIsSuccessful(Boolean isSuccessful) { this.isSuccessful = isSuccessful; }

    public Integer getAttemptNumber() { return attemptNumber; }
    public void setAttemptNumber(Integer attemptNumber) { this.attemptNumber = attemptNumber; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
