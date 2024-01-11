package com.example.codeArena.chatting.domain;

import com.example.codeArena.User.model.User;
import com.example.codeArena.chatting.dto.ChatRoomCreateRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "chat_rooms")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false, unique = true)
    private String roomId;

    @Column(nullable = false)
    private String name;

    @Column(name = "max_user_num", nullable = false)
    private int maxUserNum;

    @Column(name = "cur_user_num")
    private int curUserNum;

    @Enumerated(EnumType.STRING)
    private Tag tag;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    // TODO: User table과 연관 관계 설정 후, 방을 만든 사람에 대한 정보 추가
    @OneToOne
    private User user;

    @OneToMany(mappedBy = "chat_rooms", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatRoomUser> chatRoomUsers = new HashSet<>();


    public static ChatRoom create(ChatRoomCreateRequest dto) {
        ChatRoom room = new ChatRoom();
        room.roomId = UUID.randomUUID().toString();
        room.name = dto.getName();
        room.maxUserNum = dto.getMaxUserNum();
        room.curUserNum = 0; // 초기값 설정
        room.tag = dto.getTag();
        room.createTime = LocalDateTime.now();
        // TODO: User 추가
        return room;
    }
}
