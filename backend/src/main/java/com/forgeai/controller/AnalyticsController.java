package com.forgeai.controller;

import com.forgeai.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// REST controller delivering longitudinal engineering analytics, XP velocity, and score progression trends.
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    // Returns historical score progression, XP velocity, and skill evolution filtered by timeframe (7d, 30d, 90d).
    @GetMapping("/trends")
    public ResponseEntity<Map<String, Object>> getTrends(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "30d") String period) {

        List<Map<String, Object>> scoreHistory = new ArrayList<>();
        List<Map<String, Object>> xpGrowth = new ArrayList<>();
        List<Map<String, Object>> skillEvolution = new ArrayList<>();

        if ("7d".equalsIgnoreCase(period)) {
            scoreHistory.add(Map.of("date", "Day 1", "score", 68.5, "readiness", 64.0));
            scoreHistory.add(Map.of("date", "Day 2", "score", 69.0, "readiness", 64.5));
            scoreHistory.add(Map.of("date", "Day 3", "score", 69.8, "readiness", 65.0));
            scoreHistory.add(Map.of("date", "Day 4", "score", 70.4, "readiness", 66.0));
            scoreHistory.add(Map.of("date", "Day 5", "score", 71.0, "readiness", 66.8));
            scoreHistory.add(Map.of("date", "Day 6", "score", 71.5, "readiness", 67.2));
            scoreHistory.add(Map.of("date", "Day 7", "score", 72.0, "readiness", 68.0));

            xpGrowth.add(Map.of("day", "Mon", "xp", 200));
            xpGrowth.add(Map.of("day", "Tue", "xp", 350));
            xpGrowth.add(Map.of("day", "Wed", "xp", 150));
            xpGrowth.add(Map.of("day", "Thu", "xp", 400));
            xpGrowth.add(Map.of("day", "Fri", "xp", 300));
            xpGrowth.add(Map.of("day", "Sat", "xp", 500));
            xpGrowth.add(Map.of("day", "Sun", "xp", 250));
        } else if ("90d".equalsIgnoreCase(period)) {
            scoreHistory.add(Map.of("date", "Month 1", "score", 55.0, "readiness", 48.0));
            scoreHistory.add(Map.of("date", "Month 2", "score", 64.0, "readiness", 58.0));
            scoreHistory.add(Map.of("date", "Month 3", "score", 72.0, "readiness", 68.0));

            xpGrowth.add(Map.of("day", "Month 1", "xp", 1200));
            xpGrowth.add(Map.of("day", "Month 2", "xp", 1850));
            xpGrowth.add(Map.of("day", "Month 3", "xp", 2450));
        } else {
            // Default 30 days
            scoreHistory.add(Map.of("date", "Week 1", "score", 62.0, "readiness", 56.0));
            scoreHistory.add(Map.of("date", "Week 2", "score", 65.5, "readiness", 60.0));
            scoreHistory.add(Map.of("date", "Week 3", "score", 69.0, "readiness", 64.5));
            scoreHistory.add(Map.of("date", "Week 4", "score", 72.0, "readiness", 68.0));

            xpGrowth.add(Map.of("day", "Week 1", "xp", 550));
            xpGrowth.add(Map.of("day", "Week 2", "xp", 1100));
            xpGrowth.add(Map.of("day", "Week 3", "xp", 1750));
            xpGrowth.add(Map.of("day", "Week 4", "xp", 2450));
        }

        skillEvolution.add(Map.of("skill", "Programming", "baseline", 65, "current", 75));
        skillEvolution.add(Map.of("skill", "Problem Solving", "baseline", 60, "current", 72));
        skillEvolution.add(Map.of("skill", "Testing", "baseline", 40, "current", 58));
        skillEvolution.add(Map.of("skill", "Debugging", "baseline", 48, "current", 62));
        skillEvolution.add(Map.of("skill", "System Design", "baseline", 45, "current", 60));
        skillEvolution.add(Map.of("skill", "Security", "baseline", 50, "current", 65));

        Map<String, Object> response = new HashMap<>();
        response.put("period", period);
        response.put("scoreHistory", scoreHistory);
        response.put("xpGrowth", xpGrowth);
        response.put("skillEvolution", skillEvolution);
        response.put("isDemoData", true);
        response.put("learningConsistency", 85);
        response.put("activeDaysInPeriod", 22);

        return ResponseEntity.ok(response);
    }
}
