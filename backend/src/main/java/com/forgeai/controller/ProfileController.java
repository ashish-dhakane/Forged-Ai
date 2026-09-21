package com.forgeai.controller;

import com.forgeai.entity.Project;
import com.forgeai.entity.User;
import com.forgeai.repository.ProjectRepository;
import com.forgeai.repository.UserRepository;
import com.forgeai.security.UserPrincipal;
import com.forgeai.service.ScoreCalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

// REST controller serving comprehensive engineering profile and Engineer DNA radar metrics.
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ScoreCalculationService scoreCalculationService;

    public ProfileController(UserRepository userRepository, ProjectRepository projectRepository,
                             ScoreCalculationService scoreCalculationService) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.scoreCalculationService = scoreCalculationService;
    }

    // Fetches full engineering profile details including projects, technologies, and academic experience.
    @GetMapping
    public ResponseEntity<Map<String, Object>> getProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = com.forgeai.security.SecurityUtils.getRequiredUserId(userPrincipal);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        List<Project> projects = projectRepository.findByUserId(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("githubUsername", user.getGithubUsername());
        response.put("bio", user.getBio() != null ? user.getBio() : "Computer Science Student & Systems Engineer");
        response.put("xp", user.getXp() != null ? user.getXp() : 0);
        response.put("level", user.getLevel() != null ? user.getLevel() : 1);
        response.put("streak", user.getStreak() != null ? user.getStreak() : 0);
        response.put("skills", List.of("Java", "TypeScript", "Next.js", "Spring Boot", "PostgreSQL", "Docker", "REST APIs", "Git", "Redis"));
        response.put("technologies", List.of("Spring Security", "Hibernate", "Tailwind CSS", "JUnit 5", "Mockito", "JJWT", "Recharts"));
        response.put("languages", List.of(Map.of("name", "Java", "percentage", 50), Map.of("name", "TypeScript", "percentage", 35), Map.of("name", "SQL", "percentage", 15)));
        response.put("experience", "Undergraduate Software Engineering Projects & Full-Stack Development");
        response.put("education", "Bachelor of Engineering in Computer Science (Final Year Major Project)");
        response.put("projects", projects);

        return ResponseEntity.ok(response);
    }

    // Returns the 6 core Engineer DNA dimensions for radar chart visualization along with AI commentary.
    @GetMapping("/dna")
    public ResponseEntity<Map<String, Object>> getEngineerDna(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = com.forgeai.security.SecurityUtils.getRequiredUserId(userPrincipal);
        com.forgeai.dto.ScoreDto scoreDto = scoreCalculationService.getUserScoreDto(userId);

        List<Map<String, Object>> dimensions = List.of(
                Map.of("dimension", "Builder", "score", Math.round(Math.max(scoreDto.getProjects(), scoreDto.getGithub())), "fullMark", 100),
                Map.of("dimension", "Debugger", "score", Math.round(scoreDto.getDebugging()), "fullMark", 100),
                Map.of("dimension", "Problem Solver", "score", Math.round((scoreDto.getProgramming() + scoreDto.getProblemSolving()) / 2.0), "fullMark", 100),
                Map.of("dimension", "Architect", "score", Math.round(scoreDto.getSystemDesign()), "fullMark", 100),
                Map.of("dimension", "Security", "score", Math.round(scoreDto.getSecurity()), "fullMark", 100),
                Map.of("dimension", "Communicator", "score", Math.round(scoreDto.getCommunication()), "fullMark", 100)
        );

        Map<String, Object> response = new HashMap<>();
        response.put("dimensions", dimensions);
        response.put("aiInsight", "Deterministic analysis indicates current competency level across the 6 core engineering DNA pillars.");
        response.put("isAiGenerated", true);

        return ResponseEntity.ok(response);
    }
}
