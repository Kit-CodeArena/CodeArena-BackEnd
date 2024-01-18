package com.example.codeArena.Post.api;

import com.example.codeArena.Post.dto.CommentCreateDto;
import com.example.codeArena.Post.domain.Comment;
import com.example.codeArena.Post.service.CommentService;
import com.example.codeArena.exception.CustomException;
import com.example.codeArena.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        commentDto.setAuthorId(currentUser.getId());
        commentDto.setAuthorNickname(currentUser.getNickname());
        Comment comment = commentService.createComment(commentDto);
        return ResponseEntity.ok(comment);
    }

    // 특정 게시글의 댓글 조회
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId,
                                                 @RequestBody String content,
                                                 @AuthenticationPrincipal UserPrincipal currentUser) {
        Comment updatedComment = commentService.updateComment(commentId, content, currentUser.getId())
                .orElseThrow(() -> new CustomException(CustomException.ErrorCode.COMMENT_NOT_FOUND));
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
                                              @AuthenticationPrincipal UserPrincipal currentUser) {
        commentService.deleteComment(commentId, currentUser.getId());
        return ResponseEntity.ok().build();
    }

    // 대댓글 추가
    @PostMapping("/{commentId}/replies")
    public ResponseEntity<Comment> addReplyToComment(@PathVariable Long commentId,
                                                     @RequestBody CommentCreateDto replyDto,
                                                     @AuthenticationPrincipal UserPrincipal currentUser) {
        replyDto.setAuthorId(currentUser.getId());
        replyDto.setAuthorNickname(currentUser.getNickname());
        replyDto.setParentCommentId(commentId);
        Comment replyComment = commentService.createReply(replyDto);
        return ResponseEntity.ok(replyComment);
    }
}
