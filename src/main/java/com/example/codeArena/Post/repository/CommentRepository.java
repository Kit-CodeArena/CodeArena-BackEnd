package com.example.codeArena.Post.repository;

import com.example.codeArena.Post.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> { // ID 타입을 Long으로 변경

    // 특정 게시글에 속한 댓글 찾기
    List<Comment> findByPostId(Long postId);
}