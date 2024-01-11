package com.example.codeArena.Post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateDto {
    private String postId; // 대상 게시글 ID
    private String authorId; // 댓글 작성자 ID
    private String authorNickname; // 댓글 작성자 닉네임
    private String content; // 댓글 내용
    private String parentCommentId; // 원 댓글 ID (대댓글인 경우)
}