package com.example.codeArena.User.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenDto {
    private String token; // JWT 토큰 필드만 남김

    public TokenDto(String token) {
        this.token = token;
    }
}