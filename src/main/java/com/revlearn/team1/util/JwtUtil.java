package com.revlearn.team1.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.beans.factory.annotation.Value;
import com.revlearn.team1.model.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${SECRET_KEY}")
    private String secretKey;
    private final long validityInMilliseconds = 24 * 60 * 60 * 1000; // 1 day in milliseconds

    public String generateToken(String subject, String role) {
        if (role == null || role.isEmpty())
            role = "Student";
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validityInMilliseconds);

        Key signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("role", role)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validityInMilliseconds);

        Key signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId())) // Use user ID as the subject
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("username", user.getUsername())
                .claim("role", user.getRole())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .claim("email", user.getEmail())
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims decodeJWT(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | SignatureException |
                 WeakKeyException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid JWT token. " + e.getMessage());
        }
    }

    public String extractUsername(String token) {
        return decodeJWT(token).getSubject();
    }

    public String extractRole(String token) {
        Claims claims = decodeJWT(token);
        return claims.get("role", String.class); // Default to null if the role is not present
    }

    public boolean validateToken(String token, User user) {
        if (token == null || user == null)
            return false;
        final String username = extractUsername(token);
        if (username == null || user.getUsername() == null)
            return false;
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return decodeJWT(token).getExpiration().before(new Date());
    }
}
