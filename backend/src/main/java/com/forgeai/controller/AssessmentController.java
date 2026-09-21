package com.forgeai.controller;

import com.forgeai.dto.AssessmentResultDto;
import com.forgeai.dto.AssessmentSubmitRequest;
import com.forgeai.entity.Assessment;
import com.forgeai.entity.AssessmentResult;
import com.forgeai.security.UserPrincipal;
import com.forgeai.service.AssessmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST controller managing engineering skill assessments and question scoring.
@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {

    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    // Returns all technical assessments cataloged across engineering domains.
    @GetMapping
    public ResponseEntity<List<Assessment>> getAllAssessments() {
        return ResponseEntity.ok(assessmentService.getAllAssessments());
    }

    // Fetches a specific assessment along with its technical questions.
    @GetMapping("/{id}")
    public ResponseEntity<Assessment> getAssessmentById(@PathVariable Long id) {
        return ResponseEntity.ok(assessmentService.getAssessmentById(id));
    }

    // Submits candidate responses, evaluates correctness, and returns performance tier.
    @PostMapping("/submit")
    public ResponseEntity<AssessmentResultDto> submitAssessment(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody AssessmentSubmitRequest request) {
        Long userId = com.forgeai.security.SecurityUtils.getRequiredUserId(userPrincipal);
        return ResponseEntity.ok(assessmentService.submitAssessment(userId, request));
    }

    // Returns completed assessment history and past score breakdowns for the user.
    @GetMapping("/results")
    public ResponseEntity<List<AssessmentResult>> getUserResults(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = com.forgeai.security.SecurityUtils.getRequiredUserId(userPrincipal);
        return ResponseEntity.ok(assessmentService.getUserResults(userId));
    }
}
