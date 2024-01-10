package com.example.codeArena.Post.service;

import com.example.codeArena.Post.dto.CommentCreateDto;
import com.example.codeArena.Post.model.Comment;
import com.example.codeArena.Post.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    // 댓글 생성
    @Transactional
    public Comment createComment(CommentCreateDto createDto) {
        Comment comment = new Comment();
        comment.setPostId(createDto.getPostId());
        comment.setAuthorId(createDto.getAuthorId());
        comment.setContent(createDto.getContent());
        comment.setCreatedAt(new Date());
        // 데이터 검증 로직 추가 가능
        return commentRepository.save(comment);
    }

    // 특정 게시글의 댓글 조회
    public List<Comment> getCommentsByPostId(String postId) {
        return commentRepository.findByPostId(postId);
    }

    // 특정 댓글 조회
    public Optional<Comment> getCommentById(String commentId) {
        return commentRepository.findById(commentId);
    }

    // 댓글 수정
    @Transactional
    public Comment updateComment(String commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with id: " + commentId));
        comment.setContent(content);
        comment.setUpdatedAt(new Date());  // 댓글 수정 시간 업데이트
        // 추가적인 데이터 검증 로직 가능
        return commentRepository.save(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(String commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new IllegalArgumentException("Comment not found with id: " + commentId);
        }
        commentRepository.deleteById(commentId);
    }

    // 대댓글 추가
    @Transactional
    public Comment addReplyToComment(String commentId, String replyCommentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with id: " + commentId));
        comment.addReply(replyCommentId);
        return commentRepository.save(comment);
    }

    // 기타 필요한 메소드 추가...
}
