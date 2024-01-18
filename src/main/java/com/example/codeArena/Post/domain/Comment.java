package com.example.codeArena.Post.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 댓글 ID

    private Long postId; // 해당 댓글이 속한 게시글의 ID
    private Long authorId; // 댓글 작성자 ID (User 모델의 ID와 연결)
    private String authorNickname; // 댓글 작성자 닉네임
    private String content; // 댓글 내용

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt; // 댓글 생성 날짜
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt; // 댓글 수정 날짜

    @ElementCollection
    private List<Long> replies; // 대댓글 ID 목록

    private Long parentCommentId; // 원 댓글 ID


    // 대댓글 추가 메소드
    public void addReply(Long replyId) {
        this.replies.add(replyId);
    }
}
