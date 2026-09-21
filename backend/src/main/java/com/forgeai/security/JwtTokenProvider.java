package com.forgeai.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// Generates, signs, and validates JSON Web Tokens for stateless API authentication.
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final SecretKey key;
    private final long jwtExpirationMs;

    // Documented development-only fallback key for zero-config viva evaluation
    private static final String DEV_FALLBACK_SECRET = "ForgeAIDevOnlySecretKeyForVivaPresentation2026Min32Chars!";

    public JwtTokenProvider(
            @Value("${app.jwt.secret:}") String jwtSecret,
            @Value("${app.jwt.expiration-ms:86400000}") long jwtExpirationMs,
            @Value("${spring.profiles.active:h2}") String activeProfile) {
        
        String effectiveSecret = jwtSecret != null ? jwtSecret.trim() : "";
        boolean isProduction = "postgres".equalsIgnoreCase(activeProfile) || "prod".equalsIgnoreCase(activeProfile);

        if (isProduction) {
            if (effectiveSecret.length() < 32) {
                throw new IllegalStateException("Production startup failed: APP_JWT_SECRET environment variable is missing or insecure (must be at least 32 characters/256-bit).");
            }
        } else {
            if (effectiveSecret.length() < 32) {
                logger.warn("APP_JWT_SECRET not provided or shorter than 32 characters. Activating documented development fallback key for local demo/viva mode.");
                effectiveSecret = DEV_FALLBACK_SECRET;
            }
        }

        this.key = Keys.hmacShaKeyFor(effectiveSecret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationMs = jwtExpirationMs;
    }

    // Creates a signed JWT token containing user identity claims upon successful login.
    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(Long.toString(userPrincipal.getId()))
                .claim("email", userPrincipal.getUsername())
                .claim("name", userPrincipal.getName())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    // Generates a JWT token directly for a specific user ID and email (e.g. for Demo login).
    public String generateTokenFromUser(Long userId, String email, String name) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(Long.toString(userId))
                .claim("email", email)
                .claim("name", name)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    // Extracts the user ID from the signed JWT claims.
    public Long getUserIdFromJwt(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.parseLong(claims.getSubject());
    }

    // Verifies cryptographic signature and expiration of an incoming token.
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            logger.error("Invalid or expired JWT token: {}", ex.getMessage());
        }
        return false;
    }
}
