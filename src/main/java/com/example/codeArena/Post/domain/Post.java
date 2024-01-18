package com.example.codeArena.Post.domain;

import com.example.codeArena.User.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt; // 생성 날짜

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt; // 수정 날짜

    @ElementCollection
    private Set<String> tags = new HashSet<>(); // 태그 세트

    private int likes; // 좋아요 수
    private int views; // 조회 수

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Lob
    @Column(name = "image", columnDefinition="LONGBLOB")
    private byte[] image; // 이미지 데이터

    public Post(String title, String content, User author, Set<String> tags) {
        this.title = title;
        this.content = content;
        this.author = author; // User 객체를 직접 참조
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

    // 좋아요 감소 메소드
    public void decrementLikes() {
        this.likes--;
    }

    public void incrementViews() {
        this.views++;
    }

    // 댓글 추가 메소드
    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this); // 댓글에 게시글 설정
    }
}
