package com.example.codeArena.Post.repository;

import com.example.codeArena.Post.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    // 특정 게시글에 속한 댓글 찾기
    List<Comment> findByPostId(String postId);
}
