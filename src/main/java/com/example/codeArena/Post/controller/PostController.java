package com.example.codeArena.Post.controller;

import com.example.codeArena.Post.dto.PostCreateDto;
import com.example.codeArena.Post.model.Post;
import com.example.codeArena.Post.service.PostService;
import com.example.codeArena.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);



    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 생성
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Post> createPost(@RequestParam("title") String title,
                                           @RequestParam("content") String content,
                                           @RequestParam("tags") Set<String> tags,
                                           @RequestParam(value = "image", required = false) MultipartFile image) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            logger.error("User authentication is null or not an instance of UserPrincipal.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        UserPrincipal currentUser = (UserPrincipal) authentication.getPrincipal();
        logger.info("Authenticated user ID: {}", currentUser.getId());

        PostCreateDto createDto = new PostCreateDto(title, content, currentUser.getId(), currentUser.getNickname(), tags);
        Post post;
        try {
            post = postService.createPost(createDto, image);
        } catch (IOException e) {
            logger.error("Error while creating post", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    @PutMapping(value = "/{postId}", consumes = {"multipart/form-data"})
    public ResponseEntity<Post> updatePost(@PathVariable String postId,
                                           @ModelAttribute PostCreateDto updateDto,
                                           @RequestParam(value = "image", required = false) MultipartFile image) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            logger.error("User authentication is null or not an instance of UserPrincipal.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        UserPrincipal currentUser = (UserPrincipal) authentication.getPrincipal();
        logger.info("Authenticated user ID: {}", currentUser.getId());

        Post updatedPost = postService.updatePost(postId, currentUser.getId(), updateDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
        return ResponseEntity.ok(updatedPost);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable String postId,
                                           @AuthenticationPrincipal UserPrincipal currentUser) {
        postService.deletePost(postId, currentUser.getId());
        return ResponseEntity.ok().build();
    }

    // 특정 사용자가 작성한 게시글 조회
    @GetMapping("/user/{authorId}")
    public ResponseEntity<List<Post>> getPostsByAuthor(@PathVariable String authorId) {
        List<Post> posts = postService.getPostsByAuthor(authorId);
        return ResponseEntity.ok(posts);
    }

    // 제목으로 게시글 검색
    @GetMapping("/search/title")
    public ResponseEntity<List<Post>> getPostsByTitle(@RequestParam String title) {
        List<Post> posts = postService.getPostsByTitle(title);
        return ResponseEntity.ok(posts);
    }

    // 태그로 게시글 검색
    @GetMapping("/search/tag")
    public ResponseEntity<List<Post>> getPostsByTag(@RequestParam String tag) {
        List<Post> posts = postService.getPostsByTag(tag);
        return ResponseEntity.ok(posts);
    }

    // 게시글에 좋아요 추가
    @PostMapping("/{postId}/like")
    public ResponseEntity<Post> incrementLikes(@PathVariable String postId) {
        Post updatedPost = postService.incrementLikes(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
        return ResponseEntity.ok(updatedPost);
    }

    // 게시글 조회수 증가
    @PostMapping("/{postId}/view")
    public ResponseEntity<Post> incrementViews(@PathVariable String postId) {
        Post updatedPost = postService.incrementViews(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
        return ResponseEntity.ok(updatedPost);
    }

    // 댓글 추가
    @PostMapping("/{postId}/comment/{commentId}")
    public ResponseEntity<Post> addCommentToPost(@PathVariable String postId, @PathVariable String commentId) {
        Post updatedPost = postService.addCommentToPost(postId, commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
        return ResponseEntity.ok(updatedPost);
    }

    // 이미지 업로드
    @PostMapping("/{postId}/uploadImage")
    public ResponseEntity<Post> uploadImageToPost(@PathVariable String postId, @RequestParam("image") MultipartFile image) throws IOException {
        Post updatedPost = postService.uploadImageToPost(postId, image)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
        return ResponseEntity.ok(updatedPost);
    }
}
