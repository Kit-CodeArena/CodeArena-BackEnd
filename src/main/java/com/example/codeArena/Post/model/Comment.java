package com.example.codeArena.Post.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Comment {
    @Id
    private String id; // 댓글 ID

    private String postId; // 해당 댓글이 속한 게시글의 ID
    private String authorId; // 댓글 작성자 ID (User 모델의 ID와 연결)
    private String authorNickname; // 댓글 작성자 닉네임 (User 모델의 닉네임과 연결)
    private String content; // 댓글 내용
    private Date createdAt; // 댓글 생성 날짜
    private Date updatedAt;  // 수정 시간
    private List<String> replies; // 대댓글 ID 목록 (자기 자신의 ID 목록)
    private String parentCommentId; // 원 댓글 ID (대댓글인 경우)


    // 대댓글 추가 메소드
    public void addReply(String replyId) {
        this.replies.add(replyId);
    }
}
