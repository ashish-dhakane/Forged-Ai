package com.forgeai.controller;

import com.forgeai.dto.CodeReviewRequest;
import com.forgeai.dto.CodeReviewResponse;
import com.forgeai.security.UserPrincipal;
import com.forgeai.service.CodeReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// REST controller providing automated AI code reviews and refactoring suggestions.
@RestController
@RequestMapping("/api/code-review")
public class CodeReviewController {

    private final CodeReviewService codeReviewService;

    public CodeReviewController(CodeReviewService codeReviewService) {
        this.codeReviewService = codeReviewService;
    }

    // Analyzes a submitted code snippet for quality, bugs, security vulnerabilities, and refactoring potential.
    @PostMapping("/analyze")
    public ResponseEntity<CodeReviewResponse> analyzeCode(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CodeReviewRequest request) {
        Long userId = userPrincipal != null ? userPrincipal.getId() : 1L;
        return ResponseEntity.ok(codeReviewService.reviewCode(userId, request));
    }
}
