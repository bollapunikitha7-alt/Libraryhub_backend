package com.libraryhub_auth_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    
    private static final String SECRET =
        "LibraryHubSuperSecretKey2024xxxx";

    
    private static final long EXPIRATION = 86400000L;

    
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

   
    public String generateToken(String email,
                                String role,
                                Long userId) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(
                    new Date(System.currentTimeMillis()
                             + EXPIRATION))
                .signWith(getSigningKey(),
                          SignatureAlgorithm.HS256)
                .compact();
    }

   
    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    // ── Extract Role from Token ───────────
    public String extractRole(String token) {
        return (String) parseClaims(token).get("role");
    }

    // ── Extract UserId from Token ─────────
    public Long extractUserId(String token) {
        return ((Number) parseClaims(token)
                .get("userId")).longValue();
    }

    // ── Validate Token ────────────────────
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}