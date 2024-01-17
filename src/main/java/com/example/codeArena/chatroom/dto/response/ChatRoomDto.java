package com.example.codeArena.chatroom.dto.response;

import com.example.codeArena.chatroom.domain.ChatRoom;
import com.example.codeArena.chatroom.domain.vo.ChatRoomStatus;
import com.example.codeArena.chatroom.domain.vo.Tag;
import com.example.codeArena.chatroomuser.domain.ChatRoomUser;
import com.example.codeArena.chatroomuser.domain.vo.ChatRoomUserRole;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomDto {
    private Long id;
    private String name;
    private int maxUserNum;
    private int curUserNum;
    private Tag tag;
    private ChatRoomStatus status;
    private LocalDateTime createAt;
    private Long leaderId;

    public ChatRoomDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.name = chatRoom.getName();
        this.maxUserNum = chatRoom.getMaxUserNum();
        this.curUserNum = chatRoom.getChatRoomUsers().size();
        this.tag = chatRoom.getTag();
        this.status = chatRoom.getStatus();
        this.createAt = chatRoom.getCreatedAt();
        this.leaderId = getLeaderId(chatRoom);
    }

    private Long getLeaderId(ChatRoom chatRoom) {
        // ChatRoom에서 leader를 찾아서 ID 반환, 없으면 null
        return chatRoom.getChatRoomUsers().stream()
                .filter(user -> user.getChatRoomUserRole() == ChatRoomUserRole.LEADER)
                .findFirst()
                .map(ChatRoomUser::getUserId)
                .orElse(null);
    }
}
