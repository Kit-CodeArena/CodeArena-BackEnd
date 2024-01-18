package com.example.codeArena.User.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users") // 테이블 이름 지정
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // MySQL에 적합한 ID 타입으로 변경

    @Column(nullable = false, length = 50) // 필드 제약 조건 추가
    private String username; // 사용자 이름

    @Column(nullable = false, length = 50) // 필드 제약 조건 추가
    private String nickname; // 닉네임

    @Column(nullable = false, unique = true, length = 100) // 이메일은 고유해야 함
    private String email; // 이메일

    @Column(nullable = false, length = 100)
    private String password; // 비밀번호

    @Enumerated(EnumType.STRING) // 열거형 저장 방식 지정
    @Column(nullable = false)
    private UserRole role; // 권한
}
