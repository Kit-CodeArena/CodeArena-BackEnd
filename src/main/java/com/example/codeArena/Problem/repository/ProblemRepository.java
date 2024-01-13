package com.example.codeArena.Problem.repository;

import com.example.codeArena.Problem.model.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    // 제목으로 문제 찾기
    List<Problem> findByTitleContaining(String title);

    // 난이도로 문제 필터링
    List<Problem> findByDifficulty(String difficulty);

    // 카테고리로 문제 필터링
    List<Problem> findByCategory(String category);

    // 태그로 문제 필터링
    List<Problem> findByTagsContaining(String tag);

}
