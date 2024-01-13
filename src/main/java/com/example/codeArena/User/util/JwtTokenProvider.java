package com.example.codeArena.User.util;

import com.example.codeArena.User.model.User;
import com.example.codeArena.exception.CustomException;
import com.example.codeArena.exception.CustomException.ErrorCode;
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

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public JwtTokenProvider(@Value("${jwt.secret}") String jwtSecret,
                            @Value("${jwt.expiration.ms}") int jwtExpirationInMs) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        Claims claims = Jwts.claims().setSubject(user.getNickname());
        claims.put("role", ROLE_PREFIX + user.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getNicknameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        logger.info("클레임 주제(닉네임): {}", claims.getSubject());
        return claims.getSubject();
    }

    public boolean validateToken(String authToken) throws CustomException {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (JwtException ex) {
            logger.error("JWT 토큰 유효성 검증 실패: {}", ex.getMessage());
            throw new CustomException(ErrorCode.JWT_VALIDATION_FAILED);
        }
    }
}
