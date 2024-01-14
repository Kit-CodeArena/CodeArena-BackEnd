package com.example.codeArena.chatroom.strategy;

import static com.example.codeArena.exception.CustomException.ErrorCode.INVALID_STRATEGY;

import com.example.codeArena.chatroom.domain.ChatRoom;
import com.example.codeArena.chatroom.domain.vo.ChatRoomStatus;
import com.example.codeArena.chatroom.dto.response.ChatRoomStatusResponse;
import com.example.codeArena.chatroomuser.domain.ChatRoomUser;
import com.example.codeArena.exception.CustomException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ChatRoomStatusUpdater {
    private Map<ChatRoomStatus, ChatRoomStatusStrategy> strategies;

    public ChatRoomStatusUpdater(CloseStrategy closeStrategy, FinishStrategy finishStrategy) {
        strategies = new HashMap<>();
        strategies.put(ChatRoomStatus.CLOSE, closeStrategy);
        strategies.put(ChatRoomStatus.FINISH, finishStrategy);
    }

    @Transactional
    public void updateStatus(ChatRoom chatRoom, ChatRoomUser chatRoomUser, ChatRoomStatus status) {
        ChatRoomStatusStrategy strategy = strategies.get(status);
        if(strategy == null) {
            throw new CustomException(INVALID_STRATEGY);
        }

        strategy.execute(chatRoom, chatRoomUser);
        chatRoom.changeStatus(status);
    }
}
