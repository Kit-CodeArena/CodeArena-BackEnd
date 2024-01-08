package com.example.codeArena.Problem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemCreateDto {
    private String title;
    private String description;
    private String difficulty;
    private String inputFormat;
    private String outputFormat;
    private String sampleInput;
    private String sampleOutput;
    private Integer timeLimit;
    private Integer memoryLimit;
    private String category;
    private String[] tags;
}