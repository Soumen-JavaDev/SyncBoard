package com.sk.syncboard.util;

import com.sk.syncboard.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtil {

    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour


    private final String SECRET = "your-very-secure-32-character-long-secret-key-here!!";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().name())
                .claim("orgId", user.getOrganization().getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

//    extract email for token

    public String extractEmail(String token) {
       return  extractClaims(token).getSubject();
    }
    private Claims extractClaims (String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String email, UserDetails userDetails, String token) {
        return email.equals(userDetails.getUsername())&& !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}