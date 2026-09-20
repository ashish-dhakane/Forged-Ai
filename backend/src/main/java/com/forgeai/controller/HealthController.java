package com.forgeai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

// Health check endpoint verifying backend availability and runtime status.
@RestController
@RequestMapping("/api/health")
public class HealthController {

    // Returns the current system operational status and timestamp.
    @GetMapping
    public ResponseEntity<Map<String, Object>> checkHealth() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "ForgeAI Backend",
                "version", "1.0.0",
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}
