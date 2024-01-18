package com.example.codeArena.Problem.repository;

import com.example.codeArena.Problem.domain.Submission;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SubmissionRepository extends MongoRepository<Submission, String> {
    List<Submission> findByUserId(Long userId); // 사용자 ID로 제출 내역 찾기
    List<Submission> findByProblemId(String problemId); // 문제 ID로 제출 내역 찾기
}
