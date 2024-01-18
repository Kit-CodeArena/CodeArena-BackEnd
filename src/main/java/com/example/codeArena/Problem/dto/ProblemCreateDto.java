package com.example.codeArena.Problem.dto;

import com.example.codeArena.Problem.domain.TestCase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private Double timeLimit;
    private Integer memoryLimit;
    private String category;
    private String[] tags;
    private List<TestCase> testCases;
}