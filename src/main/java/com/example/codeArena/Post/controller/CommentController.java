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
        // 현재 인증된 사용자의 ID와 닉네임을 CommentCreateDto에 설정
        commentDto.setAuthorId(currentUser.getId());
        commentDto.setAuthorNickname(currentUser.getNickname());
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
        replyDto.setAuthorId(currentUser.getId());
        replyDto.setAuthorNickname(currentUser.getNickname());
        replyDto.setParentCommentId(commentId); // 원 댓글 ID 설정
        Comment replyComment = commentService.createReply(replyDto);
        return ResponseEntity.ok(replyComment);
    }
}