package com.example.codeArena.chatroom.dto.response;

import com.example.codeArena.chatroom.domain.ChatRoom;
import com.example.codeArena.chatroom.domain.vo.ChatRoomStatus;
import com.example.codeArena.chatroom.domain.vo.Tag;
import com.example.codeArena.chatroomuser.domain.ChatRoomUser;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomDto {
    private Long id;
    private String roomId;
    private String name;
    private int maxUserNum;
    private int curUserNum;
    private Tag tag;
    private ChatRoomStatus status;
    private LocalDateTime createAt;

    public ChatRoomDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.roomId = chatRoom.getRoomId();
        this.name = chatRoom.getName();
        this.maxUserNum = chatRoom.getMaxUserNum();
        this.curUserNum = chatRoom.getChatRoomUsers().size();
        this.tag = chatRoom.getTag();
        this.status = chatRoom.getStatus();
        this.createAt = chatRoom.getCreatedAt();
    }
}
