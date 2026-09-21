package com.forgeai;

import com.forgeai.entity.EngineeringScore;
import com.forgeai.entity.SkillGap;
import com.forgeai.entity.User;
import com.forgeai.repository.EngineeringScoreRepository;
import com.forgeai.repository.SkillGapRepository;
import com.forgeai.repository.UserRepository;
import com.forgeai.service.SkillGapService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SkillGapServiceTest {

    @Mock
    private SkillGapRepository skillGapRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EngineeringScoreRepository scoreRepository;

    @InjectMocks
    private SkillGapService skillGapService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("Jane Doe", "jane@example.com", "pass123", "janedoe");
        testUser.setId(10L);
    }

    @Test
    void testLowScoresProduceHighSeverityGaps() {
        when(userRepository.findById(10L)).thenReturn(Optional.of(testUser));
        when(skillGapRepository.findByUserId(10L)).thenReturn(Collections.emptyList());

        EngineeringScore lowScore = new EngineeringScore();
        lowScore.setTestingScore(30.0);
        lowScore.setDebuggingScore(40.0);
        lowScore.setSystemDesignScore(45.0);
        lowScore.setSecurityScore(35.0);
        lowScore.setProblemSolvingScore(42.0);
        lowScore.setGithubScore(20.0);

        when(scoreRepository.findByUserId(10L)).thenReturn(Optional.of(lowScore));
        when(skillGapRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

        List<SkillGap> gaps = skillGapService.recalculateSkillGaps(10L);

        assertEquals(6, gaps.size());
        assertTrue(gaps.stream().allMatch(g -> "HIGH".equals(g.getGapSeverity())), "Scores under 50 should have HIGH severity");
        assertTrue(gaps.stream().allMatch(g -> "Beginner".equals(g.getCurrentLevel())));
    }

    @Test
    void testHighScoresProduceLowSeverityGaps() {
        when(userRepository.findById(10L)).thenReturn(Optional.of(testUser));
        when(skillGapRepository.findByUserId(10L)).thenReturn(Collections.emptyList());

        EngineeringScore highScore = new EngineeringScore();
        highScore.setTestingScore(85.0);
        highScore.setDebuggingScore(90.0);
        highScore.setSystemDesignScore(80.0);
        highScore.setSecurityScore(78.0);
        highScore.setProblemSolvingScore(88.0);
        highScore.setGithubScore(82.0);

        when(scoreRepository.findByUserId(10L)).thenReturn(Optional.of(highScore));
        when(skillGapRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

        List<SkillGap> gaps = skillGapService.recalculateSkillGaps(10L);

        assertEquals(6, gaps.size());
        assertTrue(gaps.stream().allMatch(g -> "LOW".equals(g.getGapSeverity())), "Scores 75+ should have LOW severity");
        assertTrue(gaps.stream().allMatch(g -> "Advanced".equals(g.getCurrentLevel())));
    }
}
