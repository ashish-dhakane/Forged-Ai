package com.forgeai.controller;

import com.forgeai.dto.InterviewAnswerRequest;
import com.forgeai.dto.InterviewEvaluationResponse;
import com.forgeai.dto.InterviewStartRequest;
import com.forgeai.entity.Interview;
import com.forgeai.entity.InterviewAnswer;
import com.forgeai.security.UserPrincipal;
import com.forgeai.service.InterviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// REST controller conducting role-specific AI technical interviews and scoring student responses.
@RestController
@RequestMapping("/api/interview")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    // Starts a new mock technical interview by creating contextual questions adapted to the chosen role.
    @PostMapping("/start")
    public ResponseEntity<Interview> startInterview(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody InterviewStartRequest request) {
        Long userId = com.forgeai.security.SecurityUtils.getRequiredUserId(userPrincipal);
        return ResponseEntity.ok(interviewService.startInterview(userId, request));
    }

    // Submits candidate's answer for an interview question.
    @PostMapping("/answer")
    public ResponseEntity<InterviewAnswer> submitAnswer(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody InterviewAnswerRequest request) {
        Long userId = com.forgeai.security.SecurityUtils.getRequiredUserId(userPrincipal);
        return ResponseEntity.ok(interviewService.submitAnswer(userId, request));
    }

    // Concludes the interview and produces a comprehensive hiring readiness evaluation report.
    @PostMapping("/{id}/evaluate")
    public ResponseEntity<InterviewEvaluationResponse> evaluateInterview(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id) {
        Long userId = com.forgeai.security.SecurityUtils.getRequiredUserId(userPrincipal);
        return ResponseEntity.ok(interviewService.evaluateInterview(userId, id));
    }
}
