package com.example.codeArena.User.util;

import com.example.codeArena.User.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private static final String ROLE_PREFIX = "ROLE_";

    private final String jwtSecret;
    private final int jwtExpirationInMs;
    private SecretKey key;

    // SecretKey와 기타 초기화 작업을 위한 PostConsAtruct 메소드
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public JwtTokenProvider(@Value("${jwt.secret}") String jwtSecret,
                            @Value("${jwt.expiration.ms}") int jwtExpirationInMs) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    // JWT 토큰 생성
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        // JWT 클레임 생성
        Claims claims = Jwts.claims().setSubject(user.getNickname());
        claims.put("role", ROLE_PREFIX + user.getRole());

        // JWT 토큰 생성
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // JWT 토큰에서 닉네임 가져오기
    public String getNicknameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        logger.info("클레임 주제(닉네임): {}", claims.getSubject());
        return claims.getSubject();
    }

    // JWT 토큰 유효성 검증
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (JwtException ex) {
            logger.error("JWT 토큰 유효성 검증 실패: {}", ex.getMessage());
        }
        return false;
    }
}
