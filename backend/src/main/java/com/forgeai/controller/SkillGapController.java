package com.forgeai.controller;

import com.forgeai.entity.SkillGap;
import com.forgeai.security.UserPrincipal;
import com.forgeai.service.SkillGapService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// REST controller exposing detected engineering skill gaps and pedagogical recommendations.
@RestController
@RequestMapping("/api/skill-gaps")
public class SkillGapController {

    private final SkillGapService skillGapService;

    public SkillGapController(SkillGapService skillGapService) {
        this.skillGapService = skillGapService;
    }

    // Fetches all current skill gaps identified for the student.
    @GetMapping
    public ResponseEntity<List<SkillGap>> getSkillGaps(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal != null ? userPrincipal.getId() : 1L;
        return ResponseEntity.ok(skillGapService.getSkillGapsForUser(userId));
    }

    // Triggers an algorithmic re-evaluation of skill gaps based on latest assessment and mission submissions.
    @PostMapping("/recalculate")
    public ResponseEntity<List<SkillGap>> recalculateSkillGaps(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal != null ? userPrincipal.getId() : 1L;
        return ResponseEntity.ok(skillGapService.recalculateSkillGaps(userId));
    }
}
