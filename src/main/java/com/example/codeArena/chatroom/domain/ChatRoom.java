package com.example.codeArena.chatroom.domain;

import static com.example.codeArena.exception.CustomException.ErrorCode.IS_NOT_CLOSE;
import static com.example.codeArena.exception.CustomException.ErrorCode.IS_NOT_OPENING;

import com.example.codeArena.User.model.User;
import com.example.codeArena.chatroomuser.domain.ChatRoomUser;
import com.example.codeArena.chatroom.domain.vo.Tag;
import com.example.codeArena.chatroom.dto.request.ChatRoomCreateRequest;
import com.example.codeArena.chatroom.domain.vo.ChatRoomStatus;
import com.example.codeArena.exception.CustomException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "chat_rooms")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(name = "max_user_num", nullable = false)
    private int maxUserNum;

    @Enumerated(EnumType.STRING)
    private Tag tag;

    @Column(nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private ChatRoomStatus status;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.PERSIST)
    private Set<ChatRoomUser> chatRoomUsers = new HashSet<>();



    public static ChatRoom create(ChatRoomCreateRequest dto, User user) {
        ChatRoom room = new ChatRoom();
        room.name = dto.getName();
        room.maxUserNum = dto.getMaxUserNum();
        room.tag = dto.getTag();
        room.status = ChatRoomStatus.OPEN;
        room.createdAt = LocalDateTime.now();
        room.user = user;
        return room;
    }

    public void addRoomMember(ChatRoomUser chatRoomUser){
        chatRoomUsers.add(chatRoomUser);
    }

    public void changeStatus(ChatRoomStatus status) {
        this.status = status;
    }

    public void isNotOpening() {
        if (!(this.status == ChatRoomStatus.OPEN)) {
            throw new CustomException(IS_NOT_OPENING);
        }
    }

    public void isNotClose() {
        if (!(this.status == ChatRoomStatus.CLOSE)) {
            throw new CustomException(IS_NOT_CLOSE);
        }
    }
}
