package com.noblesse.backend.oauth2.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${JWT_SECRET}")
    private String secretKey;

    @Value("${JWT_EXPIRATION_MS}")
    private long expirationMs;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // JWT 생성
    public String generateAccessToken(Long userId) {
        return generateToken(userId, "access");
    }

    public String generateRefreshToken(Long userId) {
        return generateToken(userId, "refresh");
    }

    public String generateAdminToken(Long adminId) {
        return generateToken(adminId, "ROLE_ADMIN");
    }

    private String generateToken(Long id, String type) {
        return Jwts.builder()
                .setSubject(id.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .claim("type", type)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 검증
    public boolean validateAccessToken(String token) {
        return validateToken(token, "access");
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, "refresh");
    }

    public boolean validateAdminToken(String token) {
        return validateToken(token, "ROLE_ADMIN");
    }

    private boolean validateToken(String token, String type) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return claims.getBody().get("type").equals(type) && !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 클레임 추출
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // 사용자 ID 추출
    public Long extractUserId(String token) {
        return Long.valueOf(getClaimFromToken(token, Claims::getSubject));
    }

    public String extractType(String token) {
        return getClaimFromToken(token, claims -> claims.get("type", String.class));
    }

    public String extractUserName(String token) {
        return getClaimFromToken(token, claims -> claims.get("userName", String.class));
    }
}