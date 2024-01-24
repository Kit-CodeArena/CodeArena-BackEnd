package com.example.codeArena.Problem.api;

import com.example.codeArena.Problem.dto.ProblemCreateDto;
import com.example.codeArena.Problem.dto.ProblemDto;
import com.example.codeArena.Problem.dto.ProblemUpdateDto;
import com.example.codeArena.Problem.service.ProblemService;
import com.example.codeArena.exception.CustomException;
import static com.example.codeArena.exception.CustomException.ErrorCode;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    private static final Logger logger = LoggerFactory.getLogger(ProblemController.class);

    private final ProblemService problemService;

    @Autowired
    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    // 문제 생성
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProblemDto> createProblem(@RequestBody @Valid ProblemCreateDto createDto) {
        try {
            ProblemDto createdProblem = problemService.createProblem(createDto);
            return ResponseEntity.ok(createdProblem);
        } catch (CustomException e) {
            logger.error("문제 생성 중 예외 발생", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "문제 생성 중 오류 발생: " + e.getMessage());
        }
    }

    // 모든 문제 조회
    @GetMapping
    public ResponseEntity<List<ProblemDto>> getAllProblems() {
        try {
            List<ProblemDto> problems = problemService.getAllProblems();
            return ResponseEntity.ok(problems);
        } catch (CustomException e) {
            logger.error("문제 조회 중 예외 발생", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "문제 조회 중 오류 발생: " + e.getMessage());
        }
    }

    // 대회용 문제 조회
    @GetMapping("/contest")
    public ResponseEntity<List<ProblemDto>> getContestProblems() {
        try {
            List<ProblemDto> contestProblems = problemService.getContestProblems();
            return ResponseEntity.ok(contestProblems);
        } catch (CustomException e) {
            logger.error("대회용 문제 조회 중 예외 발생", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "대회용 문제 조회 중 오류 발생: " + e.getMessage());
        }
    }

    // 연습 문제 조회
    @GetMapping("/practice")
    public ResponseEntity<List<ProblemDto>> getPracticeProblems() {
        try {
            List<ProblemDto> practiceProblems = problemService.getPracticeProblems();
            return ResponseEntity.ok(practiceProblems);
        } catch (CustomException e) {
            logger.error("연습 문제 조회 중 예외 발생", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "연습 문제 조회 중 오류 발생: " + e.getMessage());
        }
    }

    // 특정 문제 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProblemDto> getProblemById(@PathVariable Long id) {
        try {
            ProblemDto problemDto = problemService.getProblemById(id)
                    .orElseThrow(() -> new CustomException(ErrorCode.PROBLEM_NOT_FOUND));
            return ResponseEntity.ok(problemDto);
        } catch (CustomException e) {
            logger.error("문제 조회 중 예외 발생", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "문제를 찾을 수 없습니다: " + e.getMessage());
        }
    }

    // 문제 수정
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProblemDto> updateProblem(@PathVariable Long id, @RequestBody @Valid ProblemUpdateDto updateDto) {
        try {
            ProblemDto updatedProblem = problemService.updateProblem(id, updateDto)
                    .orElseThrow(() -> new CustomException(ErrorCode.PROBLEM_NOT_FOUND));
            return ResponseEntity.ok(updatedProblem);
        } catch (CustomException e) {
            logger.error("문제 수정 중 예외 발생", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "문제 수정 중 오류 발생: " + e.getMessage());
        }
    }

    // 문제 삭제
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProblem(@PathVariable Long id) {
        try {
            problemService.deleteProblem(id);
            return ResponseEntity.ok().build();
        } catch (CustomException e) {
            logger.error("문제 삭제 중 예외 발생", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "문제 삭제 중 오류 발생: " + e.getMessage());
        }
    }

    // 제목으로 문제 필터링
    @GetMapping("/title")
    public ResponseEntity<List<ProblemDto>> getProblemsByTitle(@RequestParam String title) {
        try {
            List<ProblemDto> problems = problemService.getProblemsByTitle(title);
            return ResponseEntity.ok(problems);
        } catch (CustomException e) {
            logger.error("제목으로 문제 조회 중 예외 발생", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "제목으로 문제 조회 중 오류 발생: " + e.getMessage());
        }
    }

    // 난이도로 문제 필터링
    @GetMapping("/difficulty")
    public ResponseEntity<List<ProblemDto>> getProblemsByDifficulty(@RequestParam String difficulty) {
        try {
            List<ProblemDto> problems = problemService.getProblemsByDifficulty(difficulty);
            return ResponseEntity.ok(problems);
        } catch (CustomException e) {
            logger.error("난이도로 문제 조회 중 예외 발생", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "난이도로 문제 조회 중 오류 발생: " + e.getMessage());
        }
    }

    // 카테고리로 문제 필터링
    @GetMapping("/category")
    public ResponseEntity<List<ProblemDto>> getProblemsByCategory(@RequestParam String category) {
        try {
            List<ProblemDto> problems = problemService.getProblemsByCategory(category);
            return ResponseEntity.ok(problems);
        } catch (CustomException e) {
            logger.error("카테고리로 문제 조회 중 예외 발생", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "카테고리로 문제 조회 중 오류 발생: " + e.getMessage());
        }
    }

    // 태그로 문제 필터링
    @GetMapping("/tags")
    public ResponseEntity<List<ProblemDto>> getProblemsByTag(@RequestParam String tag) {
        try {
            List<ProblemDto> problems = problemService.getProblemsByTag(tag);
            return ResponseEntity.ok(problems);
        } catch (CustomException e) {
            logger.error("태그로 문제 조회 중 예외 발생", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "태그로 문제 조회 중 오류 발생: " + e.getMessage());
        }
    }
}
