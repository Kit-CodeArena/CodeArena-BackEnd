package com.example.codeArena.Problem.dto;

import com.example.codeArena.Problem.domain.ProblemType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDto {
    private Long id;
    private String title;
    private String description;
    private String difficulty;
    private String inputFormat;
    private String outputFormat;
    private String sampleInput;
    private String sampleOutput;
    private Double timeLimit;
    private Integer memoryLimit;
    private Integer totalSubmissions;
    private Integer correctSubmissions;
    private Double accuracy;
    private String category;
    private String[] tags;
    private ProblemType type;
}