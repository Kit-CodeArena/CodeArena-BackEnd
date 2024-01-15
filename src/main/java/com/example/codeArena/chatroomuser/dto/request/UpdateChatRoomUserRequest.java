package com.example.codeArena.chatroomuser.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateChatRoomUserRequest {
    @NotNull(message = "강퇴할 사용자를 입력해주세요.")
    Long blockUserId;
}
