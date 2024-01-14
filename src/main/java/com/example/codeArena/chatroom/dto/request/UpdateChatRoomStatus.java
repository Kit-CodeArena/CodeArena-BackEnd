package com.example.codeArena.chatroom.dto.request;

import com.example.codeArena.chatroom.domain.vo.ChatRoomStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateChatRoomStatus {
    @NotNull(message = "변경할 상태를 입력해주세요.")
    ChatRoomStatus status;
}
