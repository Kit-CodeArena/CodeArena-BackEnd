package com.example.codeArena.Post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long id; // 게시글 ID
    private String title; // 제목
    private String content; // 내용
    private String authorNickname;
    private Date createdAt; // 생성 날짜
    private Date updatedAt; // 수정 날짜
    private Set<String> tags; // 태그 세트
    private int views; // 조회 수
    private int commentCount; // 댓글 수
    private boolean isLikedByCurrentUser;
    private int likes; // 좋아요 수

    public void setIsLikedByCurrentUser(boolean isLikedByCurrentUser) {
        this.isLikedByCurrentUser = isLikedByCurrentUser;    }
}
