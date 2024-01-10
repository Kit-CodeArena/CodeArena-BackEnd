package com.example.codeArena.Post.controller;

import com.example.codeArena.Post.dto.CommentCreateDto;
import com.example.codeArena.Post.model.Comment;
import com.example.codeArena.Post.service.CommentService;
import com.example.codeArena.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 댓글 생성
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody CommentCreateDto commentDto,
                                                 @AuthenticationPrincipal UserPrincipal currentUser) {
        if (!commentDto.getAuthorId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).body(null);  // Forbidden access
        }
        Comment comment = commentService.createComment(commentDto);
        return ResponseEntity.ok(comment);
    }

    // 특정 게시글에 대한 댓글 조회
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable String postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable String commentId,
                                                 @RequestBody String content,
                                                 @AuthenticationPrincipal UserPrincipal currentUser) {
        Comment comment = commentService.getCommentById(commentId)
                .orElse(null);

        if (comment == null || !comment.getAuthorId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).body(null);  // Forbidden access
        }

        Comment updatedComment = commentService.updateComment(commentId, content);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId,
                                              @AuthenticationPrincipal UserPrincipal currentUser) {
        Comment comment = commentService.getCommentById(commentId)
                .orElse(null);

        if (comment == null || !comment.getAuthorId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).body(null);  // Forbidden access
        }

        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    // 기타 필요한 엔드포인트 추가...
}
