package com.example.codeArena.Problem.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Submission {
    @Id
    private Long id;

    private String userId; // 제출한 사용자 ID
    private String problemId; // 문제 ID
    private String code; // 제출한 코드
    private String language; // 프로그래밍 언어
    private SubmissionStatus status; // 채점 상태 (예: PENDING, ACCEPTED, REJECTED)
    private String result; // 채점 결과 설명 (예: "정답", "오답", "시간 초과")
    private Long executionTime; // 실행 시간 (밀리초 단위)

    // 채점 상태를 나타내는 열거형
    public enum SubmissionStatus {
        PENDING,
        ACCEPTED,
        REJECTED,
        ERROR
    }
}
