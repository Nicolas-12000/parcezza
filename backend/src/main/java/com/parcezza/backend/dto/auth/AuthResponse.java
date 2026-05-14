package com.parcezza.backend.dto.auth;

public record AuthResponse(String accessToken, String tokenType) {
    public AuthResponse(String accessToken) {
        this(accessToken, "Bearer");
    }
}
