package com.example.codeArena.Problem.repository;

import com.example.codeArena.Problem.model.Problem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemRepository extends MongoRepository<Problem, String> {

    // 특정 ID로 문제 찾기
    Optional<Problem> findById(String id);

    // 제목으로 문제 찾기
    List<Problem> findByTitleContaining(String title);

    // 난이도로 문제 필터링
    List<Problem> findByDifficulty(String difficulty);

    // 카테고리로 문제 필터링
    List<Problem> findByCategory(String category);

    // 태그로 문제 필터링
    List<Problem> findByTagsContaining(String tag);

    // 추가적인 쿼리 메서드가 필요하다면 여기에 구현
}