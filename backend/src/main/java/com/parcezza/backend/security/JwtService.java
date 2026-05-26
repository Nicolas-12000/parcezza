package com.parcezza.backend.security;

import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(UserDetails userDetails);
    String generateToken(UserDetails userDetails, long expirationMs);
    String generateToken(UserDetails userDetails, Map<String, Object> extraClaims);
    String extractUsername(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
}
