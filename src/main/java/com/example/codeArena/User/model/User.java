package com.example.codeArena.User.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class User {
    @Id
    private String id;

    private String username; // 사용자 이름
    private String nickname; // 닉네임
    private String email; // 이메일
    private String password; // 비밀번호
    private UserRole role; // 권한
}