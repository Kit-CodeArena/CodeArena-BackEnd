package com.example.codeArena.chatroom.strategy;

import com.example.codeArena.chatroom.domain.ChatRoom;
import com.example.codeArena.chatroomuser.domain.ChatRoomUser;
import org.springframework.stereotype.Component;

@Component
public class CloseStrategy implements ChatRoomStatusStrategy {

    @Override
    public void execute(ChatRoom chatroom, ChatRoomUser chatRoomUser) {
        chatroom.isNotOpening();
    }
}
