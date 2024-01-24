package com.example.codeArena.Problem.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionResult {
    private String output; // 실행 결과 출력
    private long executionTime; // 실행 시간 (밀리초)
    private boolean timeLimitExceeded; // 시간 초과 여부
    private boolean memoryLimitExceeded; // 메모리 초과 여부
}
