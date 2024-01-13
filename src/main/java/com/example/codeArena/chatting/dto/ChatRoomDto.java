package com.example.codeArena.chatting.dto;

import com.example.codeArena.chatting.domain.ChatRoom;
import com.example.codeArena.chatting.domain.Tag;
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
    private LocalDateTime createTime;
    private String userNickName;

    public ChatRoomDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.roomId = chatRoom.getRoomId();
        this.name = chatRoom.getName();
        this.maxUserNum = chatRoom.getMaxUserNum();
        this.curUserNum = chatRoom.getCurUserNum();
        this.tag = chatRoom.getTag();
        this.createTime = chatRoom.getCreateTime();
        this.userNickName = chatRoom.getUser().getNickname();
    }
}
