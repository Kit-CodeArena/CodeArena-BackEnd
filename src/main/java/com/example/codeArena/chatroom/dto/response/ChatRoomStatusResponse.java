package com.example.codeArena.chatroom.dto.response;

import com.example.codeArena.chatroom.domain.vo.ChatRoomStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomStatusResponse {
    ChatRoomStatus status;
}
