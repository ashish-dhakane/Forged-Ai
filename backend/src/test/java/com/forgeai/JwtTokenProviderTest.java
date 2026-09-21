package com.forgeai;

import com.forgeai.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtTokenProviderTest {

    private static final String STRONG_SECRET = "VerySecureProductionGradeSecretKey2026ForgeAIMinimum32BytesRequired!";

    @Test
    void testTokenGenerationAndValidation() {
        JwtTokenProvider provider = new JwtTokenProvider(STRONG_SECRET, 3600000L, "h2");

        String token = provider.generateTokenFromUser(42L, "test@example.com", "Alex");
        assertNotNull(token);
        assertTrue(provider.validateToken(token));
        assertEquals(42L, provider.getUserIdFromJwt(token));
    }

    @Test
    void testProductionFailsWithShortSecret() {
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            new JwtTokenProvider("short-secret", 3600000L, "postgres");
        });
        assertTrue(ex.getMessage().contains("APP_JWT_SECRET"));
    }

    @Test
    void testDevProfileAllowsFallback() {
        assertDoesNotThrow(() -> {
            JwtTokenProvider provider = new JwtTokenProvider("", 3600000L, "h2");
            String token = provider.generateTokenFromUser(1L, "dev@example.com", "Dev");
            assertTrue(provider.validateToken(token));
        });
    }
}
