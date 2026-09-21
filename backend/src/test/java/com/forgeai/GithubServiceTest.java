package com.forgeai;

import com.forgeai.entity.RepositoryEntity;
import com.forgeai.entity.User;
import com.forgeai.exception.BadRequestException;
import com.forgeai.repository.RepositoryEntityRepository;
import com.forgeai.repository.UserRepository;
import com.forgeai.service.GithubService;
import com.forgeai.service.ScoreCalculationService;
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
public class GithubServiceTest {

    @Mock
    private RepositoryEntityRepository repositoryEntityRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ScoreCalculationService scoreCalculationService;
    @Mock
    private SkillGapService skillGapService;

    @InjectMocks
    private GithubService githubService;

    private User realUser;
    private User demoUser;

    @BeforeEach
    void setUp() {
        realUser = new User("Real Dev", "real@example.com", "pass123", "realdev");
        realUser.setId(5L);

        demoUser = new User("Alex Morgan (Demo Engineer)", "demo@forgeai.dev", "Demo1234!", "demo-dev");
        demoUser.setId(1L);
    }

    @Test
    void testRealUserGetsEmptyListWhenNoReposSynced() {
        when(repositoryEntityRepository.findByUserId(5L)).thenReturn(Collections.emptyList());
        when(userRepository.findById(5L)).thenReturn(Optional.of(realUser));

        List<RepositoryEntity> repos = githubService.getUserRepositories(5L);

        assertNotNull(repos);
        assertTrue(repos.isEmpty(), "Real user without repos must receive an empty list, not demo repos");
    }

    @Test
    void testDemoUserGetsSeededRepositories() {
        when(repositoryEntityRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        when(userRepository.findById(1L)).thenReturn(Optional.of(demoUser));
        when(repositoryEntityRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

        List<RepositoryEntity> repos = githubService.getUserRepositories(1L);

        assertNotNull(repos);
        assertFalse(repos.isEmpty(), "Demo user must receive seeded repos for presentation");
        assertTrue(repos.size() >= 3);
    }

    @Test
    void testSyncThrowsWhenGithubUsernameMissing() {
        realUser.setGithubUsername("");
        when(userRepository.findById(5L)).thenReturn(Optional.of(realUser));

        assertThrows(BadRequestException.class, () -> {
            githubService.syncUserRepositories(5L, null, null);
        });
    }
}
