package com.example.codeArena.chatroomuser.service;

import static com.example.codeArena.exception.CustomException.ErrorCode.CHAT_ROOM_USER_NOT_FOUND;
import static com.example.codeArena.exception.CustomException.ErrorCode.USER_NOT_FOUND;

import com.example.codeArena.chatroomuser.domain.ChatRoomUser;
import com.example.codeArena.chatroomuser.repository.ChatRoomUserRepository;
import com.example.codeArena.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomUserService {

    private final ChatRoomUserRepository chatRoomUserRepository;

    @Transactional
    public void block(Long userId, Long blockUserId, Long roomId) {
        ChatRoomUser leader = chatRoomUserRepository.findChatRoomUsersByUserIdAndChatRoomId(userId, roomId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        leader.isNotLeader(); // leader 인지 체크

        ChatRoomUser user = chatRoomUserRepository.findChatRoomUsersByUserIdAndChatRoomId(blockUserId, roomId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (!user.isUser()) { // 채팅방의 유저 권한인지 체크
            throw new CustomException(CHAT_ROOM_USER_NOT_FOUND);
        }

        user.blockMember();
    }

    @Transactional
    public void delete(Long userId, Long roomId) {
        chatRoomUserRepository.deleteByUserIdAndChatRoomId(userId, roomId);
    }
}
