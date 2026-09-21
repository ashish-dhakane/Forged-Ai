package com.forgeai.dto;

import java.time.LocalDateTime;

// Data transfer object representing safe user profile details.
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String githubUsername;
    private String profileImage;
    private String bio;
    private Integer xp;
    private Integer level;
    private Integer streak;
    private String role;
    private Boolean isDemo;
    private LocalDateTime createdAt;

    public UserDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGithubUsername() { return githubUsername; }
    public void setGithubUsername(String githubUsername) { this.githubUsername = githubUsername; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public Integer getXp() { return xp; }
    public void setXp(Integer xp) { this.xp = xp; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Integer getStreak() { return streak; }
    public void setStreak(Integer streak) { this.streak = streak; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Boolean getIsDemo() { return isDemo; }
    public void setIsDemo(Boolean isDemo) { this.isDemo = isDemo; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
