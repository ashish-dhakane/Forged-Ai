package com.forgeai.controller;

import com.forgeai.dto.RoadmapProgressUpdateRequest;
import com.forgeai.entity.Roadmap;
import com.forgeai.entity.RoadmapItem;
import com.forgeai.security.UserPrincipal;
import com.forgeai.service.RoadmapService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// REST controller delivering the student's personalized phased engineering roadmap and progress updates.
@RestController
@RequestMapping("/api/roadmap")
public class RoadmapController {

    private final RoadmapService roadmapService;

    public RoadmapController(RoadmapService roadmapService) {
        this.roadmapService = roadmapService;
    }

    // Fetches the active 5-phase personalized engineering roadmap for the student.
    @GetMapping
    public ResponseEntity<Roadmap> getRoadmap(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = com.forgeai.security.SecurityUtils.getRequiredUserId(userPrincipal);
        return ResponseEntity.ok(roadmapService.getOrCreateUserRoadmap(userId));
    }

    // Updates progress percentage and milestone status for a specific roadmap item.
    @PatchMapping("/items/{id}/progress")
    public ResponseEntity<RoadmapItem> updateItemProgress(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id,
            @Valid @RequestBody RoadmapProgressUpdateRequest request) {
        Long userId = com.forgeai.security.SecurityUtils.getRequiredUserId(userPrincipal);
        return ResponseEntity.ok(roadmapService.updateItemProgress(userId, id, request));
    }
}
