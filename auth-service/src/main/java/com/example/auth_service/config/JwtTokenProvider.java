package com.example.auth_service.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.io.Decoders;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key secretKey;
    private final long validityInMs;

    public JwtTokenProvider(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.expiration-ms}") long validityInMs
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.validityInMs = validityInMs;
    }

    public String createToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + validityInMs);
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(exp)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException|IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(secretKey)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }
}

