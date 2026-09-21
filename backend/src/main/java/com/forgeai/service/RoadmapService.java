package com.forgeai.service;

import com.forgeai.dto.RoadmapProgressUpdateRequest;
import com.forgeai.entity.Roadmap;
import com.forgeai.entity.RoadmapItem;
import com.forgeai.entity.User;
import com.forgeai.exception.ResourceNotFoundException;
import com.forgeai.repository.RoadmapItemRepository;
import com.forgeai.repository.RoadmapRepository;
import com.forgeai.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// Generates and manages the student's dynamic 5-phase personalized engineering roadmap.
@Service
public class RoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final RoadmapItemRepository roadmapItemRepository;
    private final UserRepository userRepository;

    public RoadmapService(RoadmapRepository roadmapRepository, RoadmapItemRepository roadmapItemRepository, UserRepository userRepository) {
        this.roadmapRepository = roadmapRepository;
        this.roadmapItemRepository = roadmapItemRepository;
        this.userRepository = userRepository;
    }

    // Fetches the active roadmap for a user or initializes a customized learning plan.
    @Transactional
    public Roadmap getOrCreateUserRoadmap(Long userId) {
        return roadmapRepository.findByUserId(userId)
                .orElseGet(() -> initializeDefaultRoadmap(userId));
    }

    // Initializes a 5-phase engineering curriculum targeting industry-ready competencies.
    @Transactional
    public Roadmap initializeDefaultRoadmap(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        Roadmap roadmap = new Roadmap(user, "Personalized Full-Stack Systems Roadmap", "Industry-Ready Software Engineer");
        roadmap.setTotalPhases(5);
        roadmap.setOverallProgress(40.0);
        Roadmap savedRoadmap = roadmapRepository.save(roadmap);

        List<RoadmapItem> items = new ArrayList<>();

        // Phase 1: Programming Fundamentals & Modern Java/TypeScript
        items.add(new RoadmapItem(savedRoadmap, 1, "Phase 1: Foundations & Clean Code",
                "Clean Architecture & OOP Patterns", "Intermediate", "Advanced",
                "Refactoring Guru Design Patterns, Clean Code Principles Guide", 15, "COMPLETED", 100));
        items.add(new RoadmapItem(savedRoadmap, 1, "Phase 1: Foundations & Clean Code",
                "Advanced Data Structures & Algorithms", "Intermediate", "Advanced",
                "LeetCode Medium collection, MIT 6.006 Lecture Series", 20, "COMPLETED", 100));

        // Phase 2: Debugging & Automated Testing
        items.add(new RoadmapItem(savedRoadmap, 2, "Phase 2: Debugging & Testing",
                "Unit Testing with JUnit 5 & Mockito", "Beginner", "Intermediate",
                "Baeldung Spring Boot Testing Guide, Pragmatic Unit Testing in Java", 12, "IN_PROGRESS", 65));
        items.add(new RoadmapItem(savedRoadmap, 2, "Phase 2: Debugging & Testing",
                "Defect Isolation & Debugger Profiling", "Intermediate", "Advanced",
                "IntelliJ Profiling Masterclass, Memory Leak Identification Notes", 10, "IN_PROGRESS", 40));

        // Phase 3: System Design & Caching Architecture
        items.add(new RoadmapItem(savedRoadmap, 3, "Phase 3: System Design & Caching",
                "High-Throughput Caching with Redis", "Beginner", "Intermediate",
                "Redis University RU101, Cache-aside & Write-through Patterns", 14, "NOT_STARTED", 0));
        items.add(new RoadmapItem(savedRoadmap, 3, "Phase 3: System Design & Caching",
                "Relational vs NoSQL Schema Modeling", "Intermediate", "Advanced",
                "Designing Data-Intensive Applications (Chapters 1-4)", 18, "NOT_STARTED", 0));

        // Phase 4: Application Security & Hardening
        items.add(new RoadmapItem(savedRoadmap, 4, "Phase 4: Security & Hardening",
                "OWASP Top 10 Mitigation & JWT Hardening", "Beginner", "Intermediate",
                "OWASP Cheat Sheet Series, Spring Security in Action", 12, "NOT_STARTED", 0));
        items.add(new RoadmapItem(savedRoadmap, 4, "Phase 4: Security & Hardening",
                "Secure API Rate Limiting & Input Validation", "Beginner", "Intermediate",
                "Bucket4j Token Bucket Implementation, Hibernate Validator Guide", 8, "NOT_STARTED", 0));

        // Phase 5: Technical Interview & Industry Readiness
        items.add(new RoadmapItem(savedRoadmap, 5, "Phase 5: Technical Interview Readiness",
                "Mock System Architecture Rounds", "Intermediate", "Advanced",
                "System Design Primer by Donne Martin, ByteByteGo Case Studies", 25, "NOT_STARTED", 0));
        items.add(new RoadmapItem(savedRoadmap, 5, "Phase 5: Technical Interview Readiness",
                "Engineering Behavioral & Viva Preparation", "Intermediate", "Advanced",
                "STAR Technique Framework, Project Defense Viva Cheat Sheet", 10, "NOT_STARTED", 0));

        roadmapItemRepository.saveAll(items);
        savedRoadmap.setItems(items);
        return savedRoadmap;
    }

    // Updates progress percentage and status for a specific roadmap milestone item.
    @Transactional
    public RoadmapItem updateItemProgress(Long userId, Long itemId, RoadmapProgressUpdateRequest request) {
        RoadmapItem item = roadmapItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Roadmap item not found: " + itemId));

        if (userId != null && item.getRoadmap() != null && item.getRoadmap().getUser() != null &&
                !item.getRoadmap().getUser().getId().equals(userId)) {
            throw new org.springframework.security.access.AccessDeniedException("You do not have permission to modify this roadmap item");
        }

        item.setStatus(request.getStatus());
        item.setProgressPercentage(request.getProgressPercentage());
        RoadmapItem updatedItem = roadmapItemRepository.save(item);

        // Recalculates total roadmap completion percentage
        Roadmap roadmap = updatedItem.getRoadmap();
        List<RoadmapItem> allItems = roadmapItemRepository.findByRoadmapIdOrderByPhaseNumberAsc(roadmap.getId());
        double avgProgress = allItems.stream()
                .mapToInt(RoadmapItem::getProgressPercentage)
                .average()
                .orElse(0.0);

        roadmap.setOverallProgress(Math.round(avgProgress * 10.0) / 10.0);
        roadmapRepository.save(roadmap);

        return updatedItem;
    }
}
