package org.chronyqor.dto.auth;

public record AuthResponse(
        String token,
        String refreshToken,
        String username
) {}
