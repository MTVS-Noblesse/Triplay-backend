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
import java.util.HashMap;
import java.util.Map;

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
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userId.toString());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateRefreshToken(Long userId) {
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 검증
    public boolean validateAccessToken(String token) {
        try {
            // 토큰 파싱 및 서명 검증
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);

            // 만료 시간 검증
            Date expiration = claimsJws.getBody().getExpiration();
            if (expiration.before(new Date())) {
                return false; // 만료된 토큰
            }

            // 클레임 검증: 사용자 ID 확인
            String userId = claimsJws.getBody().get("sub", String.class);
            return userId != null;// 모든 검증 통과
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    public boolean validateRefreshToken(String token) {
        try {
            // 토큰 파싱 및 서명 검증
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);

            // 만료 시간 검증
            Date expiration = claimsJws.getBody().getExpiration();
            if (expiration.before(new Date())) {
                return false; // 만료된 토큰
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 사용자 ID 추출
    public Long extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.valueOf(claims.get("sub").toString());
    }
}
