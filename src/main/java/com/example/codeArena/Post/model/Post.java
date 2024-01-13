package com.example.codeArena.Post.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 게시글 ID

    private String title; // 제목
    private String content; // 내용

    @Column(name = "author_id")
    private Long authorId; // 작성자 ID (User 모델의 ID와 연결)

    private String authorNickname; // 작성자 닉네임

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt; // 생성 날짜

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt; // 수정 날짜

    @ElementCollection
    private Set<String> tags = new HashSet<>(); // 태그 세트

    private int likes; // 좋아요 수
    private int views; // 조회 수

    @ElementCollection
    private List<Long> comments = new ArrayList<>(); // 댓글 ID 목록 (Comment 모델의 ID와 연결)

    @Lob
    private byte[] image; // 이미지 데이터

    public Post(String title, String content, Long authorId, String authorNickname, Set<String> tags) {
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
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
    public void addComment(Long commentId) {
        this.comments.add(commentId);
    }
}
