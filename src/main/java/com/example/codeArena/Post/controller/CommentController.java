package com.example.codeArena.Post.controller;

import com.example.codeArena.Post.dto.CommentCreateDto;
import com.example.codeArena.Post.model.Comment;
import com.example.codeArena.Post.service.CommentService;
import com.example.codeArena.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    // 댓글 생성
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody CommentCreateDto commentDto,
                                                 @AuthenticationPrincipal UserPrincipal currentUser) {
        if (!currentUser.getId().equals(commentDto.getAuthorId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "댓글 생성 권한이 없습니다.");
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
        Comment updatedComment = commentService.updateComment(commentId, content, currentUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId,
                                              @AuthenticationPrincipal UserPrincipal currentUser) {
        commentService.deleteComment(commentId, currentUser.getId());
        return ResponseEntity.ok().build();
    }

    // 대댓글 추가
    @PostMapping("/{commentId}/replies")
    public ResponseEntity<Comment> addReplyToComment(@PathVariable String commentId,
                                                     @RequestBody CommentCreateDto replyDto,
                                                     @AuthenticationPrincipal UserPrincipal currentUser) {
        if (!currentUser.getId().equals(replyDto.getAuthorId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "대댓글 생성 권한이 없습니다.");
        }
        Comment replyComment = commentService.createComment(replyDto);
        Comment updatedComment = commentService.addReplyToComment(commentId, replyComment.getId());
        return ResponseEntity.ok(updatedComment);
    }
}