package com.example.codeArena.chatroomuser.dto.response;

import com.example.codeArena.User.model.User;
import com.example.codeArena.chatroomuser.domain.ChatRoomUser;
import com.example.codeArena.chatroomuser.domain.vo.ChatRoomUserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomUserResponse {
    private Long id;
    private Long userId;
    private String nickname;
    private ChatRoomUserRole chatRoomUserRole;

    public static ChatRoomUserResponse of(ChatRoomUser chatRoomUser) {
        return ChatRoomUserResponse.builder()
                .id(chatRoomUser.getId())
                .userId(chatRoomUser.getUser().getId())
                .nickname(chatRoomUser.getUser().getNickname())
                .chatRoomUserRole(chatRoomUser.getChatRoomUserRole())
                .build();
    }
}
