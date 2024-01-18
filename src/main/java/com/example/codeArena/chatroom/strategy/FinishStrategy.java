package com.example.codeArena.chatroom.strategy;

import com.example.codeArena.User.repository.UserRepository;
import com.example.codeArena.chatroom.domain.ChatRoom;
import com.example.codeArena.chatroomuser.domain.ChatRoomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FinishStrategy implements ChatRoomStatusStrategy{

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void execute(ChatRoom chatroom, ChatRoomUser chatRoomUser) {
        chatroom.isNotClose();

        // 사용자의 리더, 참가 횟수 설정
//        chatroom.getChatRoomUsers().forEach(c -> {
//            User user = userRepository.findById(c.getUserId()).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
//            user.updateLeaderCount(c);
//            user.updateChatRoomCount();
//        });

    }
}
