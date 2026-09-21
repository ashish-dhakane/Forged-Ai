package com.forgeai.controller;

import com.forgeai.dto.DebuggingSubmitRequest;
import com.forgeai.dto.DebuggingSubmitResponse;
import com.forgeai.entity.DebuggingChallenge;
import com.forgeai.security.UserPrincipal;
import com.forgeai.service.DebuggingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST controller exposing interactive debugging puzzles and evaluating student diagnostic fixes.
@RestController
@RequestMapping("/api/debugging")
public class DebuggingController {

    private final DebuggingService debuggingService;

    public DebuggingController(DebuggingService debuggingService) {
        this.debuggingService = debuggingService;
    }

    // Fetches all debugging challenges across Easy, Medium, and Hard difficulty levels.
    @GetMapping("/challenges")
    public ResponseEntity<List<DebuggingChallenge>> getAllChallenges() {
        return ResponseEntity.ok(debuggingService.getAllChallenges());
    }

    // Fetches a single debugging challenge with its buggy code and descriptions.
    @GetMapping("/challenges/{id}")
    public ResponseEntity<DebuggingChallenge> getChallengeById(@PathVariable Long id) {
        return ResponseEntity.ok(debuggingService.getChallengeById(id));
    }

    // Submits the student's root-cause explanation and corrected code for automated evaluation.
    @PostMapping("/challenges/{id}/submit")
    public ResponseEntity<DebuggingSubmitResponse> submitFix(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id,
            @Valid @RequestBody DebuggingSubmitRequest request) {
        Long userId = com.forgeai.security.SecurityUtils.getRequiredUserId(userPrincipal);
        return ResponseEntity.ok(debuggingService.submitFix(userId, id, request));
    }
}
