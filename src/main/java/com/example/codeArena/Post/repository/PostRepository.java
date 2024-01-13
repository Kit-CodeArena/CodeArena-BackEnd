package com.example.codeArena.Post.repository;

import com.example.codeArena.Post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 특정 사용자가 작성한 게시글 찾기
    List<Post> findByAuthorId(Long authorId);

    // 제목으로 게시글 검색
    List<Post> findByTitleContaining(String title);

    List<Post> findByTagsContaining(String tag);
}
