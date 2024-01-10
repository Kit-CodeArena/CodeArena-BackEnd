package com.example.codeArena.Post.service;

import com.example.codeArena.Post.dto.CommentCreateDto;
import com.example.codeArena.Post.model.Comment;
import com.example.codeArena.Post.repository.CommentRepository;
import com.example.codeArena.exception.UnauthorizedAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    // 댓글 생성
    @Transactional
    public Comment createComment(CommentCreateDto createDto) {
        Comment comment = new Comment();
        comment.setPostId(createDto.getPostId());
        comment.setAuthorId(createDto.getAuthorId());
        comment.setContent(createDto.getContent());
        comment.setCreatedAt(new Date());
        return commentRepository.save(comment);
    }

    // 특정 게시글의 댓글 조회
    public List<Comment> getCommentsByPostId(String postId) {
        return commentRepository.findByPostId(postId);
    }

    // 댓글 수정
    @Transactional
    public Optional<Comment> updateComment(String commentId, String newContent, String currentUserId) {
        return commentRepository.findById(commentId).map(comment -> {
            if (!comment.getAuthorId().equals(currentUserId)) {
                throw new UnauthorizedAccessException("댓글 수정 권한이 없습니다.");
            }
            comment.setContent(newContent);
            comment.setUpdatedAt(new Date());
            return commentRepository.save(comment);
        });
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(String commentId, String currentUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 댓글을 찾을 수 없습니다: " + commentId));

        if (!comment.getAuthorId().equals(currentUserId)) {
            throw new IllegalArgumentException("댓글을 삭제할 권한이 없습니다.");
        }

        commentRepository.deleteById(commentId);
    }

    // 대댓글 추가
    @Transactional
    public Comment addReplyToComment(String commentId, String replyCommentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 댓글을 찾을 수 없습니다: " + commentId));
        comment.addReply(replyCommentId);
        return commentRepository.save(comment);
    }
}
