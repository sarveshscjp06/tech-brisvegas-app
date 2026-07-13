package com.brisvegastech.registration.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.util.Date;
import javax.crypto.SecretKey;

@Service
public class JwtService {
    // Standard secure 256-bit key for HMAC-SHA
    private final SecretKey secretKey = Keys.hmacShaKeyFor("YourSuperSecretKeyThatIsAtLeast32BytesLong!".getBytes());
    private final long expirationTime = 86400000; // 24 Hours

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, SIG.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                    .getPayload().getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}