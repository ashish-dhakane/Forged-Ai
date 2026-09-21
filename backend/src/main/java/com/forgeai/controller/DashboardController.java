package com.forgeai.controller;

import com.forgeai.dto.ScoreDto;
import com.forgeai.security.SecurityUtils;
import com.forgeai.security.UserPrincipal;
import com.forgeai.service.ScoreCalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

// REST controller delivering data snapshots for the primary student dashboard.
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final ScoreCalculationService scoreCalculationService;

    public DashboardController(ScoreCalculationService scoreCalculationService) {
        this.scoreCalculationService = scoreCalculationService;
    }

    // Fetches the user's composite Engineering Score, Industry Readiness score, and category breakdown.
    @GetMapping("/overview")
    public ResponseEntity<ScoreDto> getDashboardOverview(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = SecurityUtils.getRequiredUserId(userPrincipal);
        return ResponseEntity.ok(scoreCalculationService.getUserScoreDto(userId));
    }

    // Returns current recommended learning focus area and pedagogical rationale.
    @GetMapping("/focus")
    public ResponseEntity<Map<String, String>> getCurrentFocus(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = SecurityUtils.getRequiredUserId(userPrincipal);
        ScoreDto scoreDto = scoreCalculationService.getUserScoreDto(userId);

        Map<String, String> response = new HashMap<>();
        response.put("currentFocus", scoreDto.getCurrentFocus());
        response.put("focusReason", scoreDto.getFocusReason());

        return ResponseEntity.ok(response);
    }
}
