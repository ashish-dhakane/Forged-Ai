package com.forgeai.dto;

// Response payload returned upon successful user authentication.
public class AuthResponse {

    private String token;
    private String tokenType = "Bearer";
    private Long id;
    private String name;
    private String email;
    private String githubUsername;
    private Integer xp;
    private Integer level;
    private Integer streak;
    private String role;
    private Boolean isDemo;

    public AuthResponse() {}

    public AuthResponse(String token, Long id, String name, String email, String githubUsername, Integer xp, Integer level, Integer streak, String role, Boolean isDemo) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.email = email;
        this.githubUsername = githubUsername;
        this.xp = xp;
        this.level = level;
        this.streak = streak;
        this.role = role;
        this.isDemo = isDemo;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGithubUsername() { return githubUsername; }
    public void setGithubUsername(String githubUsername) { this.githubUsername = githubUsername; }

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
}
