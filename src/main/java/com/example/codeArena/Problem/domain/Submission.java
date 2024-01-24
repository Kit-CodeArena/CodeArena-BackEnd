package com.example.codeArena.Problem.domain;

import com.example.codeArena.User.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "submissions")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 제출한 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem; // 문제

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String code; // 제출한 코드

    private String language; // 프로그래밍 언어

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status; // 채점 상태

    private String result; // 채점 결과 설명

    private Long executionTime; // 실행 시간 (밀리초 단위)

    // 채점 상태를 나타내는 열거형
    public enum SubmissionStatus {
        PENDING,
        ACCEPTED,
        REJECTED,
        ERROR
    }
}
