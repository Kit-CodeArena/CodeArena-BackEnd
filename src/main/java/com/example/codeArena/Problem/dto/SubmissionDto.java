package com.example.codeArena.Problem.dto;

import com.example.codeArena.Problem.domain.Submission.SubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionDto {
    private Long id;
    private String userId;
    private String problemId;
    private String code;
    private String language;
    private SubmissionStatus status;
    private String result;
    private Long executionTime;
}
