package com.example.codeArena.Post.controller;

import com.example.codeArena.Post.dto.PostCreateDto;
import com.example.codeArena.Post.model.Post;
import com.example.codeArena.Post.service.PostService;
import com.example.codeArena.exception.CustomException;
import com.example.codeArena.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

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
            throw new CustomException(CustomException.ErrorCode.INVALID_CONTEXT);
        }

        UserPrincipal currentUser = (UserPrincipal) authentication.getPrincipal();
        PostCreateDto createDto = new PostCreateDto(title, content, currentUser.getId(), currentUser.getNickname(), tags);

        try {
            Post post = postService.createPost(createDto, image);
            return ResponseEntity.ok(post);
        } catch (IOException e) {
            logger.error("이미지 처리 중 오류 발생", e);
            throw new CustomException(CustomException.ErrorCode.IMAGE_PROCESSING_FAILED);
        }
    }

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // 특정 게시글 조회
    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId) {
        return postService.getPostById(postId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CustomException(CustomException.ErrorCode.POST_NOT_FOUND));
    }

    // 게시글 수정
    @PutMapping(value = "/{postId}", consumes = {"multipart/form-data"})
    public ResponseEntity<Post> updatePost(@PathVariable Long postId,
                                           @ModelAttribute PostCreateDto updateDto,
                                           @RequestParam(value = "image", required = false) MultipartFile image) {
        UserPrincipal currentUser = getCurrentUser();
        try {
            Post updatedPost = postService.updatePost(postId, currentUser.getId(), updateDto, image)
                    .orElseThrow(() -> new CustomException(CustomException.ErrorCode.POST_NOT_FOUND));
            return ResponseEntity.ok(updatedPost);
        } catch (IOException e) {
            logger.error("이미지 처리 중 오류 발생", e);
            throw new CustomException(CustomException.ErrorCode.IMAGE_PROCESSING_FAILED);
        }
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        UserPrincipal currentUser = getCurrentUser();
        postService.deletePost(postId, currentUser.getId());
        return ResponseEntity.ok().build();
    }

    // 특정 사용자가 작성한 게시글 조회
    @GetMapping("/user/{authorId}")
    public ResponseEntity<List<Post>> getPostsByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(postService.getPostsByAuthor(authorId));
    }

    // 제목으로 게시글 검색
    @GetMapping("/search/title")
    public ResponseEntity<List<Post>> getPostsByTitle(@RequestParam String title) {
        return ResponseEntity.ok(postService.getPostsByTitle(title));
    }

    // 태그로 게시글 검색
    @GetMapping("/search/tag")
    public ResponseEntity<List<Post>> getPostsByTag(@RequestParam String tag) {
        return ResponseEntity.ok(postService.getPostsByTag(tag));
    }

    // 게시글에 좋아요 추가
    @PostMapping("/{postId}/like")
    public ResponseEntity<Post> incrementLikes(@PathVariable Long postId) {
        Post updatedPost = postService.incrementLikes(postId)
                .orElseThrow(() -> new CustomException(CustomException.ErrorCode.POST_NOT_FOUND));
        return ResponseEntity.ok(updatedPost);
    }

    // 게시글 조회수 증가
    @PostMapping("/{postId}/view")
    public ResponseEntity<Post> incrementViews(@PathVariable Long postId) {
        Post updatedPost = postService.incrementViews(postId)
                .orElseThrow(() -> new CustomException(CustomException.ErrorCode.POST_NOT_FOUND));
        return ResponseEntity.ok(updatedPost);
    }

    // 댓글 추가
    @PostMapping("/{postId}/comment/{commentId}")
    public ResponseEntity<Post> addCommentToPost(@PathVariable Long postId, @PathVariable Long commentId) {
        Post updatedPost = postService.addCommentToPost(postId, commentId)
                .orElseThrow(() -> new CustomException(CustomException.ErrorCode.POST_NOT_FOUND));
        return ResponseEntity.ok(updatedPost);
    }

    // 이미지 업로드
    @PostMapping("/{postId}/uploadImage")
    public ResponseEntity<Post> uploadImageToPost(@PathVariable Long postId,
                                                  @RequestParam("image") MultipartFile image) throws IOException {
        Post updatedPost = postService.uploadImageToPost(postId, image)
                .orElseThrow(() -> new CustomException(CustomException.ErrorCode.POST_NOT_FOUND));
        return ResponseEntity.ok(updatedPost);
    }
    private UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            throw new CustomException(CustomException.ErrorCode.INVALID_CONTEXT);
        }
        return (UserPrincipal) authentication.getPrincipal();
    }
}