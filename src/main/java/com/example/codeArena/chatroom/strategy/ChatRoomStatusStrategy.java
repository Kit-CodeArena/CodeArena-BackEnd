package com.example.codeArena.chatroom.strategy;

import com.example.codeArena.chatroom.domain.ChatRoom;
import com.example.codeArena.chatroomuser.domain.ChatRoomUser;

public interface ChatRoomStatusStrategy {
    void execute(ChatRoom chatroom, ChatRoomUser chatRoomUser);
}
