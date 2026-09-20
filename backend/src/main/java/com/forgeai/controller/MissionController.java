package com.forgeai.controller;

import com.forgeai.dto.MissionSubmitRequest;
import com.forgeai.entity.Mission;
import com.forgeai.entity.MissionSubmission;
import com.forgeai.security.UserPrincipal;
import com.forgeai.service.MissionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST controller managing practical engineering missions, assignments, and XP rewards.
@RestController
@RequestMapping("/api/missions")
public class MissionController {

    private final MissionService missionService;

    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    // Fetches all available practical engineering missions.
    @GetMapping
    public ResponseEntity<List<Mission>> getAllMissions() {
        return ResponseEntity.ok(missionService.getAllMissions());
    }

    // Fetches the authenticated user's mission submissions and completed tasks.
    @GetMapping("/submissions")
    public ResponseEntity<List<MissionSubmission>> getUserSubmissions(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal != null ? userPrincipal.getId() : 1L;
        return ResponseEntity.ok(missionService.getUserSubmissions(userId));
    }

    // Submits verification details for a mission, marks it complete, and awards XP.
    @PostMapping("/{id}/submit")
    public ResponseEntity<MissionSubmission> submitMission(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id,
            @Valid @RequestBody MissionSubmitRequest request) {
        Long userId = userPrincipal != null ? userPrincipal.getId() : 1L;
        return ResponseEntity.ok(missionService.submitMission(userId, id, request));
    }
}
