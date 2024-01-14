package com.example.codeArena.chatting.controller;

import static com.example.codeArena.exception.CustomException.ErrorCode.CHAT_ROOM_NOT_FOUND;

import com.example.codeArena.chatting.domain.ChatMessage;
import com.example.codeArena.chatting.domain.ChatMessage.MessageType;
import com.example.codeArena.chatting.domain.ChatRoom;
import com.example.codeArena.chatting.repository.ChatRoomRepository;
import com.example.codeArena.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;
    private final ChatRoomRepository chatRoomRepository;

    @MessageMapping("/chat/message") // => /app/chat/message
    public void enter(ChatMessage message) {
        if(MessageType.ENTER.equals(message.getType())) {
            handleUserEntry(message);
            message.setMessage(message.getSender() + "님이 입장하였습니다.");
        }
        sendingOperations.convertAndSend("/topic/chat/room/" + message.getRoomId(), message);
    }

    /**
     * TODO : 리팩토링, 기능 변환 필요
     */
    private void handleUserEntry(ChatMessage message) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(message.getRoomId())
                .orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND));

        // 현재 사용자 수가 최대 사용자 수보다 작을 때만 증가
        if (chatRoom.getCurUserNum() < chatRoom.getMaxUserNum()) {
            chatRoom.setCurUserNum(chatRoom.getCurUserNum() + 1);
            chatRoomRepository.save(chatRoom);
            log.info("채팅방 입장 인원수 : " + chatRoom.getCurUserNum());
        } else {
            sendErrorMessage(message.getRoomId(), "채팅방에 더 이상 입장할 수 없습니다.");
        }
    }

    private void sendErrorMessage(String roomId, String errorMessage) {
        ChatMessage errorResponse = new ChatMessage();
        errorResponse.setType(MessageType.ERROR);
        errorResponse.setRoomId(roomId);
        errorResponse.setMessage(errorMessage);
        sendingOperations.convertAndSend("/topic/chat/error/" + roomId, errorResponse);
    }
}
