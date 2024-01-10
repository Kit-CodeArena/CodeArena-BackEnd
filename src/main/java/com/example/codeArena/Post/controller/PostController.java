package com.example.codeArena.Post.controller;

import com.example.codeArena.Post.dto.PostCreateDto;
import com.example.codeArena.Post.model.Post;
import com.example.codeArena.Post.service.PostService;
import com.example.codeArena.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // 게시글 생성
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestParam("title") String title,
                                           @RequestParam("content") String content,
                                           @RequestParam("authorId") String authorId,
                                           @RequestParam("tags") Set<String> tags,
                                           @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        PostCreateDto createDto = new PostCreateDto(title, content, authorId, tags);
        Post post = postService.createPost(createDto, image);
        return ResponseEntity.ok(post);
    }

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 특정 게시글 조회
    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable String postId) {
        return postService.getPostById(postId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable String postId,
                                           @AuthenticationPrincipal UserPrincipal currentUser,
                                           @RequestBody PostCreateDto updateDto) {
        Post updatedPost = postService.updatePost(postId, currentUser.getId(), updateDto);
        return ResponseEntity.ok(updatedPost);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable String postId,
                                           @AuthenticationPrincipal UserPrincipal currentUser) {
        postService.deletePost(postId, currentUser.getId());
        return ResponseEntity.ok().build();
    }

    // 기타 필요한 엔드포인트 추가...
}
