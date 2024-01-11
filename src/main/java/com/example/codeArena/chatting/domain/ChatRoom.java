package com.example.codeArena.chatting.domain;

import com.example.codeArena.User.model.User;
import com.example.codeArena.chatting.dto.ChatRoomCreateRequest;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoom {
    private String roomId;
    private String name;
    private int maxUserNum;
    private int curUserNum;
    private Tag tag;
    private LocalDateTime createTime;

    // TODO: User table과 연관 관계 설정 후, 방을 만든 사람에 대한 정보 추가
//    @OneToOne
//    private User user;


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
