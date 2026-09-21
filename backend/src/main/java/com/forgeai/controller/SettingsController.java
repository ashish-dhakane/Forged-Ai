package com.forgeai.controller;

import com.forgeai.dto.SettingsUpdateRequest;
import com.forgeai.entity.User;
import com.forgeai.repository.UserRepository;
import com.forgeai.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

// REST controller managing student profile preferences, external API configurations, and Demo Mode toggles.
@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    private final UserRepository userRepository;

    @Value("${app.ai.api-key:}")
    private String configuredAiKey;

    @Value("${app.github.token:}")
    private String configuredGithubToken;

    public SettingsController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Returns current developer platform configurations and integration states.
    @GetMapping
    public ResponseEntity<Map<String, Object>> getSettings(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = com.forgeai.security.SecurityUtils.getRequiredUserId(userPrincipal);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        Map<String, Object> response = new HashMap<>();
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("bio", user.getBio() != null ? user.getBio() : "");
        response.put("githubUsername", user.getGithubUsername() != null ? user.getGithubUsername() : "");
        response.put("hasGithubToken", (configuredGithubToken != null && !configuredGithubToken.isBlank()));
        response.put("hasAiApiKey", (configuredAiKey != null && !configuredAiKey.isBlank()));
        response.put("demoModeActive", "demo@forgeai.dev".equalsIgnoreCase(user.getEmail()));

        return ResponseEntity.ok(response);
    }

    // Updates profile details and platform preferences.
    @PutMapping
    public ResponseEntity<Map<String, String>> updateSettings(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody SettingsUpdateRequest request) {
        Long userId = com.forgeai.security.SecurityUtils.getRequiredUserId(userPrincipal);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getGithubUsername() != null) {
            user.setGithubUsername(request.getGithubUsername());
        }

        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Settings updated successfully"));
    }
}
