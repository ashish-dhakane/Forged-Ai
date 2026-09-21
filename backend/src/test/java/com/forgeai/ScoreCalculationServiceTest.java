package com.forgeai;

import com.forgeai.entity.EngineeringScore;
import com.forgeai.entity.User;
import com.forgeai.repository.*;
import com.forgeai.service.ScoreCalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Verifies deterministic Engineering Score weights and Industry Readiness calculations.
@ExtendWith(MockitoExtension.class)
public class ScoreCalculationServiceTest {

    @Mock
    private EngineeringScoreRepository scoreRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AssessmentResultRepository assessmentResultRepository;
    @Mock
    private DebuggingAttemptRepository debuggingAttemptRepository;
    @Mock
    private MissionSubmissionRepository missionSubmissionRepository;
    @Mock
    private RepositoryEntityRepository repositoryEntityRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private InterviewRepository interviewRepository;
    @Mock
    private CodeReviewRepository codeReviewRepository;
    @Mock
    private com.forgeai.service.SkillGapService skillGapService;

    @InjectMocks
    private ScoreCalculationService scoreCalculationService;

    private User sampleUser;
    private User demoUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User("Test Engineer", "engineer@test.com", "pass123", "testdev");
        sampleUser.setId(1L);
        sampleUser.setStreak(5);

        demoUser = new User("Alex Morgan (Demo Engineer)", "demo@forgeai.dev", "Demo1234!", "demo-dev");
        demoUser.setId(2L);
        demoUser.setStreak(7);
    }

    // Tests that the deterministic scoring formula applies correct weights totaling 100%.
    @Test
    void testDeterministicWeightsTotalOneHundredPercent() {
        double totalWeight = ScoreCalculationService.WEIGHT_PROGRAMMING
                + ScoreCalculationService.WEIGHT_PROBLEM_SOLVING
                + ScoreCalculationService.WEIGHT_PROJECTS
                + ScoreCalculationService.WEIGHT_GITHUB
                + ScoreCalculationService.WEIGHT_DEBUGGING
                + ScoreCalculationService.WEIGHT_TESTING
                + ScoreCalculationService.WEIGHT_SYSTEM_DESIGN
                + ScoreCalculationService.WEIGHT_SECURITY
                + ScoreCalculationService.WEIGHT_COMMUNICATION
                + ScoreCalculationService.WEIGHT_CONSISTENCY;

        assertEquals(1.0, totalWeight, 0.0001, "All 10 weights must sum precisely to 1.0 (100%)");
    }

    // Tests that score calculation produces non-null, bounded score results for a real user.
    @Test
    void testCalculateAndSaveScoresProducesValidRangesForRealUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(scoreRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(assessmentResultRepository.findByUserIdOrderByCompletedAtDesc(1L)).thenReturn(Collections.emptyList());
        when(debuggingAttemptRepository.findByUser(sampleUser)).thenReturn(Collections.emptyList());
        when(missionSubmissionRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        when(repositoryEntityRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        when(projectRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        when(interviewRepository.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(Collections.emptyList());
        when(codeReviewRepository.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(Collections.emptyList());
        when(scoreRepository.save(any(EngineeringScore.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EngineeringScore calculated = scoreCalculationService.calculateAndSaveScores(1L);

        assertNotNull(calculated);
        assertTrue(calculated.getOverallScore() >= 0 && calculated.getOverallScore() <= 100);
        assertTrue(calculated.getIndustryReadinessScore() >= 0 && calculated.getIndustryReadinessScore() <= 100);
        assertNotNull(calculated.getCurrentFocus());
        assertNotNull(calculated.getFocusReason());
        verify(skillGapService).recalculateSkillGaps(1L);
    }

    // Tests that demo user calculation maintains calibrated demonstration baseline.
    @Test
    void testCalculateAndSaveScoresPreservesDemoBaseline() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(demoUser));
        when(scoreRepository.findByUserId(2L)).thenReturn(Optional.empty());
        when(assessmentResultRepository.findByUserIdOrderByCompletedAtDesc(2L)).thenReturn(Collections.emptyList());
        when(debuggingAttemptRepository.findByUser(demoUser)).thenReturn(Collections.emptyList());
        when(missionSubmissionRepository.findByUserId(2L)).thenReturn(Collections.emptyList());
        when(repositoryEntityRepository.findByUserId(2L)).thenReturn(Collections.emptyList());
        when(projectRepository.findByUserId(2L)).thenReturn(Collections.emptyList());
        when(scoreRepository.save(any(EngineeringScore.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EngineeringScore calculated = scoreCalculationService.calculateAndSaveScores(2L);

        assertNotNull(calculated);
        assertTrue(calculated.getOverallScore() >= 65.0, "Demo user baseline should be calibrated above 65");
        assertEquals(75.0, calculated.getProgrammingScore());
        assertEquals(72.0, calculated.getProblemSolvingScore());
    }
}
