package com.example.codeArena.Problem.controller;

import com.example.codeArena.Problem.dto.ProblemCreateDto;
import com.example.codeArena.Problem.dto.ProblemDto;
import com.example.codeArena.Problem.dto.ProblemUpdateDto;
import com.example.codeArena.Problem.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    // 문제 생성
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProblemDto> createProblem(@RequestBody ProblemCreateDto createDto) {
        ProblemDto createdProblem = problemService.createProblem(createDto);
        return ResponseEntity.ok(createdProblem);
    }

    // 모든 문제 조회
    @GetMapping
    public ResponseEntity<List<ProblemDto>> getAllProblems() {
        List<ProblemDto> problems = problemService.getAllProblems();
        return ResponseEntity.ok(problems);
    }

    // 특정 문제 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProblemDto> getProblemById(@PathVariable String id) {
        return problemService.getProblemById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 문제 수정
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProblemDto> updateProblem(@PathVariable String id,
                                                    @RequestBody ProblemUpdateDto updateDto) {
        return problemService.updateProblem(id, updateDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 문제 삭제
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProblem(@PathVariable String id) {
        problemService.deleteProblem(id);
        return ResponseEntity.ok().build();
    }
}