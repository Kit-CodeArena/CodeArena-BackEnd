package com.example.codeArena.Post.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Post {
    @Id
    private String id; // 게시글 ID

    private String title; // 제목
    private String content; // 내용
    private String authorId; // 작성자 ID (User 모델의 ID와 연결)
    private Date createdAt; // 생성 날짜
    private Date updatedAt; // 수정 날짜
    private Set<String> tags; // 태그 세트
    private int likes; // 좋아요 수
    private int views; // 조회 수
    private List<String> comments; // 댓글 ID 목록 (Comment 모델의 ID와 연결)
    private byte[] image; // 이미지 데이터

    public Post(String title, String content, String authorId, Set<String> tags) {
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.tags = tags;
        this.createdAt = new Date();
        this.updatedAt = this.createdAt;
        this.likes = 0;
        this.views = 0;
        this.comments = new ArrayList<>();
    }

    // 이미지 추가 메소드
    public void addImage(byte[] image) {
        this.image = image;
    }

    // 좋아요와 조회 수 증가 메소드
    public void incrementLikes() {
        this.likes++;
    }

    public void incrementViews() {
        this.views++;
    }

    // 댓글 추가 메소드
    public void addComment(String commentId) {
        this.comments.add(commentId);
    }
}
