package com.example.codeArena.Problem.repository;

import com.example.codeArena.Problem.domain.Submission;
import com.example.codeArena.User.domain.User;
import com.example.codeArena.Problem.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByUser(User user); // 사용자 엔티티로 제출 내역 찾기
    List<Submission> findByProblem(Problem problem); // 문제 엔티티로 제출 내역 찾기
}
