package com.example.codeArena.chatroom.dto.response;

import com.example.codeArena.chatroom.domain.ChatRoom;
import com.example.codeArena.chatroom.domain.vo.ChatRoomStatus;
import com.example.codeArena.chatroom.domain.vo.Tag;
import com.example.codeArena.chatroomuser.dto.response.ChatRoomUserResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomDetailDto {
    private Long id;
    private String name;
    private int maxUserNum;
    private int curUserNum;
    private Tag tag;
    private ChatRoomStatus status;
    private LocalDateTime createAt;
    List<ChatRoomUserResponse> users;

    public static ChatRoomDetailDto of(ChatRoom chatRoom, List<ChatRoomUserResponse> users) {
        return ChatRoomDetailDto.builder()
                .id(chatRoom.getId())
                .name(chatRoom.getName())
                .maxUserNum(chatRoom.getMaxUserNum())
                .curUserNum(chatRoom.getChatRoomUsers().size())
                .tag(chatRoom.getTag())
                .status(chatRoom.getStatus())
                .createAt(chatRoom.getCreatedAt())
                .users(users)
                .build();
    }
}
