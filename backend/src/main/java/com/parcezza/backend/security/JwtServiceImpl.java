package com.parcezza.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {

    private final JwtProperties properties;

    public JwtServiceImpl(JwtProperties properties) {
        this.properties = properties;
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, Map.of(), properties.getExpirationMs());
    }

    @Override
    public String generateToken(UserDetails userDetails, long expirationMs) {
        return generateToken(userDetails, Map.of(), expirationMs);
    }

    @Override
    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        return generateToken(userDetails, extraClaims, properties.getExpirationMs());
    }

    private String generateToken(UserDetails userDetails, Map<String, Object> extraClaims, long expirationMs) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
            .claims(extraClaims)
            .subject(userDetails.getUsername())
            .issuedAt(now)
            .expiration(expiration)
            .signWith(getSigningKey())
            .compact();
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = properties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
