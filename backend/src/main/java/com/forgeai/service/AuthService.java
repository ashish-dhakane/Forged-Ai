package com.forgeai.service;

import com.forgeai.dto.AuthRequest;
import com.forgeai.dto.AuthResponse;
import com.forgeai.dto.RegisterRequest;
import com.forgeai.entity.User;
import com.forgeai.exception.BadRequestException;
import com.forgeai.repository.UserRepository;
import com.forgeai.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Handles user registration, credentials verification, JWT issuance, and instant demo login.
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final ScoreCalculationService scoreCalculationService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider,
                       ScoreCalculationService scoreCalculationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.scoreCalculationService = scoreCalculationService;
    }

    // Registers a new student account, hashes the password, and creates initial engineering baseline scores.
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered: " + request.getEmail());
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setGithubUsername(request.getGithubUsername() != null ? request.getGithubUsername() : "");
        user.setXp(100);
        user.setLevel(1);
        user.setStreak(1);

        User savedUser = userRepository.save(user);

        // Initializes the user's starting engineering score baseline
        scoreCalculationService.calculateAndSaveScores(savedUser.getId());

        String token = tokenProvider.generateTokenFromUser(savedUser.getId(), savedUser.getEmail(), savedUser.getName());

        return new AuthResponse(token, savedUser.getId(), savedUser.getName(), savedUser.getEmail(),
                savedUser.getGithubUsername(), savedUser.getXp(), savedUser.getLevel(), savedUser.getStreak(),
                savedUser.getRole(), false);
    }

    // Validates credentials via AuthenticationManager and issues a signed JWT token upon success.
    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User record not found"));

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(),
                user.getGithubUsername(), user.getXp(), user.getLevel(), user.getStreak(),
                user.getRole(), false);
    }

    // Provides one-click instant login for viva demonstrations using the pre-seeded demo engineer account.
    @Transactional
    public AuthResponse loginDemoUser() {
        User demoUser = userRepository.findByEmail("demo@forgeai.dev")
                .orElseGet(() -> {
                    User newUser = new User("Alex Morgan (Demo Engineer)", "demo@forgeai.dev",
                            passwordEncoder.encode("Demo1234!"), "demo-engineer");
                    newUser.setBio("Sample Engineering Student Profile (Final-Year Viva Demonstration Mode)");
                    newUser.setXp(2450);
                    newUser.setLevel(4);
                    newUser.setStreak(7);
                    return userRepository.save(newUser);
                });

        String token = tokenProvider.generateTokenFromUser(demoUser.getId(), demoUser.getEmail(), demoUser.getName());

        return new AuthResponse(token, demoUser.getId(), demoUser.getName(), demoUser.getEmail(),
                demoUser.getGithubUsername(), demoUser.getXp(), demoUser.getLevel(), demoUser.getStreak(),
                demoUser.getRole(), true);
    }
}
