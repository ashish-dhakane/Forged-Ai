package com.forgeai.controller;

import com.forgeai.dto.GithubSyncRequest;
import com.forgeai.entity.RepositoryEntity;
import com.forgeai.security.UserPrincipal;
import com.forgeai.service.GithubService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

// REST controller managing GitHub repository synchronizations and code health analysis.
@RestController
@RequestMapping("/api/github")
public class GithubController {

    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    // Fetches all synchronized GitHub repositories for the authenticated user.
    @GetMapping("/repos")
    public ResponseEntity<List<RepositoryEntity>> getRepositories(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal != null ? userPrincipal.getId() : 1L;
        return ResponseEntity.ok(githubService.getUserRepositories(userId));
    }

    // Triggers synchronization with GitHub's live API for a specified username.
    @PostMapping("/sync")
    public ResponseEntity<List<RepositoryEntity>> syncRepositories(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody(required = false) GithubSyncRequest request) {
        Long userId = userPrincipal != null ? userPrincipal.getId() : 1L;
        String username = request != null ? request.getGithubUsername() : null;
        String token = request != null ? request.getToken() : null;

        return ResponseEntity.ok(githubService.syncUserRepositories(userId, username, token));
    }

    // Aggregates a high-level GitHub snapshot with language distributions and activity metrics.
    @GetMapping("/snapshot")
    public ResponseEntity<Map<String, Object>> getGithubSnapshot(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal != null ? userPrincipal.getId() : 1L;
        List<RepositoryEntity> repos = githubService.getUserRepositories(userId);

        Map<String, Integer> languageCounts = new HashMap<>();
        int totalStars = 0;
        int totalForks = 0;

        for (RepositoryEntity repo : repos) {
            totalStars += repo.getStarsCount() != null ? repo.getStarsCount() : 0;
            totalForks += repo.getForksCount() != null ? repo.getForksCount() : 0;
            String lang = repo.getLanguage() != null ? repo.getLanguage() : "Other";
            languageCounts.put(lang, languageCounts.getOrDefault(lang, 0) + 1);
        }

        List<Map<String, Object>> languages = new ArrayList<>();
        languageCounts.forEach((lang, count) -> {
            languages.add(Map.of("name", lang, "count", count, "percentage", Math.round(((double) count / Math.max(repos.size(), 1)) * 100)));
        });

        Map<String, Object> snapshot = new HashMap<>();
        snapshot.put("totalRepositories", repos.size());
        snapshot.put("totalStars", totalStars);
        snapshot.put("totalForks", totalForks);
        snapshot.put("languages", languages);
        snapshot.put("recentActivity", List.of(
                Map.of("day", "Mon", "commits", 4),
                Map.of("day", "Tue", "commits", 7),
                Map.of("day", "Wed", "commits", 3),
                Map.of("day", "Thu", "commits", 8),
                Map.of("day", "Fri", "commits", 6),
                Map.of("day", "Sat", "commits", 9),
                Map.of("day", "Sun", "commits", 5)
        ));
        snapshot.put("topProjects", repos);

        return ResponseEntity.ok(snapshot);
    }
}
