package com.example.codeArena.Post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateDto {
    private String title; // 게시글 제목
    private String content; // 게시글 내용
    private String authorId; // 작성자 ID
    private Set<String> tags; // 태그 세트
}
