package com.example.codeArena.Problem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Problem {
    @Id
    private String id; // 문제 번호

    private String title; // 문제 제목
    private String description; // 문제 설명
    private String difficulty; // 문제 난이도
    private String inputFormat; // 입력 형식
    private String outputFormat; // 출력 형식
    private String sampleInput;  // 샘플 입력
    private String sampleOutput; // 샘플 출력
    private Integer timeLimit; // 시간 제한
    private Integer memoryLimit; // 메모리 제한
    private Integer totalSubmissions = 0; // 총 제출 인원
    private Integer correctSubmissions = 0; // 정답 인원
    private Double accuracy = 0.0; // 정답율
    private String category; // 카테고리
    private List<String> tags; // 태그
}
