package com.example.codeArena.chatroomuser.domain;

import static com.example.codeArena.chatroomuser.domain.vo.ChatRoomUserRole.BLOCKED;
import static com.example.codeArena.chatroomuser.domain.vo.ChatRoomUserRole.LEADER;
import static com.example.codeArena.chatroomuser.domain.vo.ChatRoomUserRole.USER;
import static com.example.codeArena.exception.CustomException.ErrorCode.CHAT_ROOM_USER_ILLEGAL_ROLE;

import com.example.codeArena.User.model.User;
import com.example.codeArena.chatroom.domain.ChatRoom;
import com.example.codeArena.chatroomuser.domain.vo.ChatRoomUserRole;
import com.example.codeArena.exception.CustomException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "chat_room_users")
public class ChatRoomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatRoomUserRole chatRoomUserRole;

    @Builder
    public ChatRoomUser(Long userId, ChatRoom chatRoom, ChatRoomUserRole chatRoomUserRole) {
        this.userId = userId;
        this.chatRoom = chatRoom;
        this.chatRoomUserRole = chatRoomUserRole;
        this.createdAt = LocalDateTime.now();
    }

    public void blockMember() {
        this.chatRoomUserRole = BLOCKED;
    }

    public boolean isLeader() {
        return this.chatRoomUserRole == LEADER;
    }

    public void isNotLeader() {
        if (!this.getChatRoomUserRole().equals(ChatRoomUserRole.LEADER)) {
            throw new CustomException(CHAT_ROOM_USER_ILLEGAL_ROLE);
        }
    }

    public boolean isMember() {
        return this.chatRoomUserRole == USER;
    }

    public boolean isBlocked() {
        return this.chatRoomUserRole == BLOCKED;
    }

}
