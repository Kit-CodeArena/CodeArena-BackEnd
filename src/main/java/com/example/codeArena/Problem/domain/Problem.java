package com.example.codeArena.Problem.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "problems") // 데이터베이스 테이블 이름 지정
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // MySQL에 적합한 ID 타입으로 변경 (문제 번호)

    @Column(nullable = false)
    private String title; // 문제 제목

    @Lob
    private String description; // 문제 설명

    private String difficulty; // 문제 난이도

    @Lob
    private String inputFormat; // 입력 형식

    @Lob
    private String outputFormat; // 출력 형식

    private String sampleInput;  // 샘플 입력
    private String sampleOutput; // 샘플 출력

    private Double timeLimit = 0.0; // 시간 제한
    private Integer memoryLimit; // 메모리 제한

    private Integer totalSubmissions = 0; // 총 제출 인원
    private Integer correctSubmissions = 0; // 정답 인원

    private Double accuracy = 0.0; // 정답율

    private String category; // 카테고리

    @ElementCollection
    private List<String> tags; // 태그

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCase> testCases = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProblemType type;

}
