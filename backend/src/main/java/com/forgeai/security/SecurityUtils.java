package com.forgeai.security;

import org.springframework.security.access.AccessDeniedException;

// Utility methods for retrieving and verifying authenticated user identities.
public final class SecurityUtils {

    private SecurityUtils() {}

    // Extracts the user ID from the UserPrincipal or throws AccessDeniedException if unauthenticated.
    public static Long getRequiredUserId(UserPrincipal userPrincipal) {
        if (userPrincipal == null || userPrincipal.getId() == null) {
            throw new AccessDeniedException("Authentication required: valid session or Bearer token missing");
        }
        return userPrincipal.getId();
    }
}
