package com.example.codeArena.Post.repository;

import com.example.codeArena.Post.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    // 특정 사용자가 작성한 게시글 찾기
    List<Post> findByAuthorId(String authorId);

    // 제목으로 게시글 검색
    List<Post> findByTitleContaining(String title);

    // 태그를 포함하는 게시글 찾기
    List<Post> findByTagsContaining(String tag);
}