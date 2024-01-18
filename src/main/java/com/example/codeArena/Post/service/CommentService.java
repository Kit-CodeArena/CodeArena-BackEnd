package com.example.codeArena.Post.service;

import com.example.codeArena.Post.dto.CommentCreateDto;
import com.example.codeArena.Post.domain.Comment;
import com.example.codeArena.Post.repository.CommentRepository;
import com.example.codeArena.User.domain.User;
import com.example.codeArena.User.repository.UserRepository;
import com.example.codeArena.Post.domain.Post;
import com.example.codeArena.Post.repository.PostRepository;
import com.example.codeArena.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    // 댓글 생성
    @Transactional
    public Comment createComment(CommentCreateDto createDto) {
        User author = userRepository.findById(createDto.getAuthorId())
                .orElseThrow(() -> new CustomException(CustomException.ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(createDto.getPostId())
                .orElseThrow(() -> new CustomException(CustomException.ErrorCode.POST_NOT_FOUND));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setAuthorNickname(createDto.getAuthorNickname());
        comment.setContent(createDto.getContent());
        comment.setCreatedAt(new Date());

        return commentRepository.save(comment);
    }

    // 특정 게시글의 댓글 조회
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    // 댓글 수정
    @Transactional
    public Optional<Comment> updateComment(Long commentId, String newContent, Long currentUserId) {
        return commentRepository.findById(commentId).map(comment -> {
            if (!comment.getAuthor().getId().equals(currentUserId)) {
                throw new CustomException(CustomException.ErrorCode.ACCESS_DENIED);
            }
            comment.setContent(newContent);
            comment.setUpdatedAt(new Date());
            return commentRepository.save(comment);
        });
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long currentUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(CustomException.ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getAuthor().getId().equals(currentUserId)) {
            throw new CustomException(CustomException.ErrorCode.ACCESS_DENIED);
        }

        commentRepository.deleteById(commentId);
    }

    // 대댓글 생성
    @Transactional
    public Comment createReply(CommentCreateDto replyDto) {
        User author = userRepository.findById(replyDto.getAuthorId())
                .orElseThrow(() -> new CustomException(CustomException.ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(replyDto.getPostId())
                .orElseThrow(() -> new CustomException(CustomException.ErrorCode.POST_NOT_FOUND));

        Comment reply = new Comment();
        reply.setPost(post);
        reply.setAuthor(author);
        reply.setAuthorNickname(replyDto.getAuthorNickname());
        reply.setContent(replyDto.getContent());
        reply.setCreatedAt(new Date());
        reply.setParentCommentId(replyDto.getParentCommentId()); // 원 댓글 ID 설정

        return commentRepository.save(reply);
    }
}
