package com.example.codeArena.Problem.api;

import com.example.codeArena.Problem.dto.SubmissionDto;
import com.example.codeArena.Problem.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @PostMapping
    public ResponseEntity<SubmissionDto> createSubmission(@RequestBody SubmissionDto submissionDto) {
        SubmissionDto createdSubmission = submissionService.createSubmission(submissionDto);
        return ResponseEntity.ok(createdSubmission);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubmissionDto>> getSubmissionsByUser(@PathVariable Long userId) {
        List<SubmissionDto> submissions = submissionService.getSubmissionsByUser(userId);
        return ResponseEntity.ok(submissions);
    }
}
