package com.example.codeArena.Post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private Long id; // 댓글 ID
    private String authorNickname; // 댓글 작성자 닉네임
    private String content; // 내용
    private Date createdAt; // 생성 날짜
}