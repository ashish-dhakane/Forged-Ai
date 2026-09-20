package com.forgeai.dto;

// Request payload for synchronizing repositories for a designated GitHub username.
public class GithubSyncRequest {

    private String githubUsername;
    private String token;

    public GithubSyncRequest() {}

    public GithubSyncRequest(String githubUsername, String token) {
        this.githubUsername = githubUsername;
        this.token = token;
    }

    public String getGithubUsername() { return githubUsername; }
    public void setGithubUsername(String githubUsername) { this.githubUsername = githubUsername; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
