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

    public static ChatRoomUserResponse of(User user, ChatRoomUser chatRoomUser) {
        return ChatRoomUserResponse.builder()
                .id(chatRoomUser.getId())
                .userId(user.getId())
                .nickname(user.getNickname())
                .chatRoomUserRole(chatRoomUser.getChatRoomUserRole())
                .build();
    }
}
