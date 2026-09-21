package com.forgeai.controller;

import com.forgeai.dto.AuthRequest;
import com.forgeai.dto.AuthResponse;
import com.forgeai.dto.RegisterRequest;
import com.forgeai.dto.UserDto;
import com.forgeai.entity.User;
import com.forgeai.repository.UserRepository;
import com.forgeai.security.UserPrincipal;
import com.forgeai.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// REST controller managing student authentication, account registration, and instant viva demo login.
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    // Registers a new user account with hashed password and generates initial JWT token.
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    // Authenticates registered users with email and password and returns JWT token.
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // One-click demo login endpoint for university viva and instant project evaluation.
    @PostMapping("/demo-login")
    public ResponseEntity<AuthResponse> demoLogin() {
        return ResponseEntity.ok(authService.loginDemoUser());
    }

    // Returns the currently authenticated user's profile details.
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return ResponseEntity.status(401).build();
        }

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setGithubUsername(user.getGithubUsername());
        dto.setProfileImage(user.getProfileImage());
        dto.setBio(user.getBio());
        dto.setXp(user.getXp());
        dto.setLevel(user.getLevel());
        dto.setStreak(user.getStreak());
        dto.setRole(user.getRole());
        dto.setIsDemo("demo@forgeai.dev".equalsIgnoreCase(user.getEmail()));
        dto.setCreatedAt(user.getCreatedAt());

        return ResponseEntity.ok(dto);
    }
}
