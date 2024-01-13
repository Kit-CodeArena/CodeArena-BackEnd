package com.example.codeArena.Problem.service;

import com.example.codeArena.Problem.dto.SubmissionDto;
import com.example.codeArena.Problem.model.Submission;
import com.example.codeArena.Problem.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    public SubmissionDto createSubmission(SubmissionDto submissionDto) {
        Submission submission = new Submission();
        submission.setUserId(submissionDto.getUserId());
        submission.setProblemId(submissionDto.getProblemId());
        submission.setCode(submissionDto.getCode());
        submission.setLanguage(submissionDto.getLanguage());
        submission.setStatus(Submission.SubmissionStatus.PENDING);

        // 채점 로직 (추후 구현)

        Submission savedSubmission = submissionRepository.save(submission);
        return convertToDto(savedSubmission);
    }

    public List<SubmissionDto> getSubmissionsByUser(Long userId) {
        return submissionRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private SubmissionDto convertToDto(Submission submission) {
        return new SubmissionDto(
                submission.getId(),
                submission.getUserId(),
                submission.getProblemId(),
                submission.getCode(),
                submission.getLanguage(),
                submission.getStatus(),
                submission.getResult(),
                submission.getExecutionTime()
        );
    }
}
