package com.forgeai.service;

import com.forgeai.entity.RepositoryEntity;
import com.forgeai.entity.User;
import com.forgeai.exception.BadRequestException;
import com.forgeai.exception.ResourceNotFoundException;
import com.forgeai.repository.RepositoryEntityRepository;
import com.forgeai.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

// Interfaces with GitHub REST API to synchronize repositories, language distribution, and code health metrics.
@Service
public class GithubService {

    private static final Logger logger = LoggerFactory.getLogger(GithubService.class);

    private final RepositoryEntityRepository repositoryEntityRepository;
    private final UserRepository userRepository;
    private final ScoreCalculationService scoreCalculationService;
    private final SkillGapService skillGapService;
    private final RestTemplate restTemplate;

    @Value("${app.github.token:}")
    private String defaultGithubToken;

    public GithubService(RepositoryEntityRepository repositoryEntityRepository,
                         UserRepository userRepository,
                         ScoreCalculationService scoreCalculationService,
                         SkillGapService skillGapService) {
        this.repositoryEntityRepository = repositoryEntityRepository;
        this.userRepository = userRepository;
        this.scoreCalculationService = scoreCalculationService;
        this.skillGapService = skillGapService;
        this.restTemplate = new RestTemplate();
    }

    // Fetches all synchronized GitHub repositories for a given user.
    public List<RepositoryEntity> getUserRepositories(Long userId) {
        List<RepositoryEntity> repos = repositoryEntityRepository.findByUserId(userId);
        if (repos.isEmpty()) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null && "demo@forgeai.dev".equalsIgnoreCase(user.getEmail())) {
                return seedFallbackRepositories(user);
            }
            return Collections.emptyList();
        }
        return repos;
    }

    // Queries live GitHub API for a username, parses metadata & health scores, and persists repositories.
    @Transactional
    public List<RepositoryEntity> syncUserRepositories(Long userId, String customUsername, String customToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        boolean isDemoUser = "demo@forgeai.dev".equalsIgnoreCase(user.getEmail());

        String username = (customUsername != null && !customUsername.isBlank())
                ? customUsername.trim()
                : (user.getGithubUsername() != null ? user.getGithubUsername().trim() : "");

        if (username.isBlank()) {
            if (isDemoUser) {
                username = "demo-engineer";
            } else {
                throw new BadRequestException("GitHub username is required for repository synchronization");
            }
        }

        // For demo engineer account with demo username, seed predictable viva presentation data
        if (isDemoUser && "demo-engineer".equalsIgnoreCase(username)) {
            user.setGithubUsername("demo-engineer");
            userRepository.save(user);
            List<RepositoryEntity> demoRepos = seedFallbackRepositories(user);
            scoreCalculationService.calculateAndSaveScores(userId);
            skillGapService.recalculateSkillGaps(userId);
            return demoRepos;
        }

        // Live GitHub API Synchronization
        List<RepositoryEntity> syncedRepos = new ArrayList<>();
        String token = (customToken != null && !customToken.isBlank()) ? customToken.trim() : defaultGithubToken;

        try {
            String url = "https://api.github.com/users/" + username + "/repos?sort=updated&per_page=15";
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "ForgeAI-Platform");
            headers.set("Accept", "application/vnd.github.v3+json");

            if (token != null && !token.isBlank()) {
                headers.set("Authorization", "Bearer " + token);
            }

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // Clear existing synced records for fresh update
                repositoryEntityRepository.deleteAll(repositoryEntityRepository.findByUserId(userId));

                for (Map<String, Object> item : response.getBody()) {
                    String name = (String) item.get("name");
                    String fullName = (String) item.get("full_name");
                    String description = (String) item.get("description");
                    String language = (String) item.get("language");
                    Integer stars = item.get("stargazers_count") instanceof Number
                            ? ((Number) item.get("stargazers_count")).intValue() : 0;
                    Integer forks = item.get("forks_count") instanceof Number
                            ? ((Number) item.get("forks_count")).intValue() : 0;
                    String htmlUrl = (String) item.get("html_url");
                    Boolean isFork = item.get("fork") instanceof Boolean ? (Boolean) item.get("fork") : false;

                    RepositoryEntity repo = buildRepositoryEntity(user, name, fullName, description, language, stars, forks, htmlUrl, isFork);
                    syncedRepos.add(repositoryEntityRepository.save(repo));
                }

                user.setGithubUsername(username);
                userRepository.save(user);
            }
        } catch (HttpClientErrorException.NotFound ex) {
            logger.warn("GitHub user not found: {}", username);
            throw new ResourceNotFoundException("GitHub user not found: '" + username + "'. Please check the username.");
        } catch (HttpClientErrorException.Forbidden ex) {
            logger.warn("GitHub API rate limit exceeded or access forbidden for user: {}", username);
            throw new BadRequestException("GitHub API rate limit reached. Please configure a GITHUB_TOKEN or wait before trying again.");
        } catch (HttpClientErrorException.Unauthorized ex) {
            logger.warn("GitHub API unauthorized for user: {}", username);
            throw new BadRequestException("Invalid GitHub access token provided. Please check credentials.");
        } catch (ResourceAccessException ex) {
            logger.warn("GitHub API connection timeout/network failure: {}", ex.getMessage());
            throw new BadRequestException("Unable to connect to GitHub API. Please check internet connection.");
        } catch (HttpStatusCodeException ex) {
            logger.warn("GitHub API returned error status {}: {}", ex.getStatusCode().value(), ex.getStatusText());
            throw new BadRequestException("GitHub API error (" + ex.getStatusCode().value() + "): " + ex.getStatusText());
        } catch (BadRequestException | ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected GitHub synchronization failure: {}", ex.getMessage());
            throw new BadRequestException("GitHub synchronization failed: " + ex.getMessage());
        }

        // Trigger score and skill gap recalculation based on new repository evidence
        scoreCalculationService.calculateAndSaveScores(userId);
        skillGapService.recalculateSkillGaps(userId);

        return syncedRepos;
    }

    // Calculates repository health score and generates automated code health insights.
    private RepositoryEntity buildRepositoryEntity(User user, String name, String fullName, String description,
                                                   String language, Integer stars, Integer forks, String htmlUrl, Boolean isFork) {
        String lang = language != null && !language.isBlank() ? language : "General";
        double healthScore = 65.0;

        if (description != null && description.trim().length() >= 10) healthScore += 10.0;
        if (!"General".equals(lang)) healthScore += 10.0;
        if (stars > 0) healthScore += 5.0;
        if (stars >= 5) healthScore += 5.0;
        if (forks > 0) healthScore += 5.0;
        if (!Boolean.TRUE.equals(isFork)) healthScore += 5.0;

        healthScore = Math.min(100.0, healthScore);

        String techDetected = lang + ", Git, REST APIs" +
                (description != null && description.toLowerCase().contains("docker") ? ", Docker" : "") +
                (description != null && description.toLowerCase().contains("spring") ? ", Spring Boot" : "");

        String strengths = "Active public GitHub repository||" + lang + " language distribution||" +
                (stars > 0 ? stars + " community stars recorded" : "Clean repository naming and modularity");

        String improvements = "Add automated continuous integration workflow (GitHub Actions)||" +
                (stars == 0 ? "Add comprehensive README and API documentation" : "Expand unit test suite and integration coverage");

        return new RepositoryEntity(user, name, fullName, description != null ? description : "",
                lang, stars, forks, htmlUrl != null ? htmlUrl : "", healthScore, techDetected, strengths, improvements);
    }

    // Creates realistic fallback repositories for zero-config demonstration mode.
    public List<RepositoryEntity> seedFallbackRepositories(User user) {
        repositoryEntityRepository.deleteAll(repositoryEntityRepository.findByUserId(user.getId()));

        List<RepositoryEntity> list = new ArrayList<>();
        list.add(new RepositoryEntity(user, "forgeai-platform", "ashish-dhakane/forgeai-platform",
                "AI-Powered Engineering Growth Platform with Next.js, Spring Boot, and PostgreSQL",
                "TypeScript", 14, 4, "https://github.com/ashish-dhakane/forgeai-platform", 88.0,
                "Next.js, TypeScript, Spring Boot, PostgreSQL, Tailwind CSS",
                "Modular full-stack separation||Comprehensive REST API endpoints||Stateless JWT security",
                "Add automated integration tests with Testcontainers||Implement Redis caching tier"));

        list.add(new RepositoryEntity(user, "distributed-task-worker", "ashish-dhakane/distributed-task-worker",
                "Fault-tolerant asynchronous task scheduler built with Java, Redis, and Spring Web",
                "Java", 23, 7, "https://github.com/ashish-dhakane/distributed-task-worker", 92.0,
                "Java 21, Spring Boot, Redis, JUnit 5, Docker",
                "High concurrency resilience||Clean modular thread management||Comprehensive unit tests",
                "Add OpenTelemetry distributed tracing||Introduce dead-letter queue recovery"));

        list.add(new RepositoryEntity(user, "microservices-auth-hub", "ashish-dhakane/microservices-auth-hub",
                "OAuth2 and JWT authentication gateway with rate limiting and role-based access control",
                "Java", 9, 2, "https://github.com/ashish-dhakane/microservices-auth-hub", 82.0,
                "Java, Spring Security, BCrypt, PostgreSQL",
                "Robust token validation||BCrypt password hashing||Configurable CORS filters",
                "Enforce token revocation blacklist in Redis||Add multi-factor authentication"));

        return repositoryEntityRepository.saveAll(list);
    }
}
