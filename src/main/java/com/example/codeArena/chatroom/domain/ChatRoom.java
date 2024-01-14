package com.example.codeArena.chatroom.domain;

import com.example.codeArena.User.model.User;
import com.example.codeArena.chat.domain.ChatRoomUser;
import com.example.codeArena.chat.domain.Tag;
import com.example.codeArena.chat.dto.ChatRoomCreateRequest;
import com.example.codeArena.chatroom.domain.vo.ChatRoomStatus;
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

    @Column(nullable = false, length = 30)
    private String name;

    @Column(name = "max_user_num", nullable = false)
    private int maxUserNum;

    @Column(name = "cur_user_num")
    private int curUserNum;

    @Enumerated(EnumType.STRING)
    private Tag tag;

    @Column(nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private ChatRoomStatus status;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @OneToOne // 사용자는 권한을 얻으면 채팅방을 1개만 만들 수 있게 한다.
    private User user;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.PERSIST)
    private Set<ChatRoomUser> chatRoomUsers = new HashSet<>();


    public static ChatRoom create(ChatRoomCreateRequest dto, User user) {
        ChatRoom room = new ChatRoom();
        room.roomId = UUID.randomUUID().toString();
        room.name = dto.getName();
        room.maxUserNum = dto.getMaxUserNum();
        room.curUserNum = 0; // 초기값 설정
        room.tag = dto.getTag();
        room.createTime = LocalDateTime.now();
        room.user = user;
        return room;
    }
}
