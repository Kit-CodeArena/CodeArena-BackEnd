package com.example.codeArena.chatting.config;

import com.example.codeArena.chatting.domain.ChatRoom;
import com.example.codeArena.chatting.dto.ChatMessage;
import com.example.codeArena.chatting.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    private ChatRoom chatRoom;
    private String roomId;

    // 웹소켓 연결 시
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocket Connection");
    }

    // 양방향 데이터 통신
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload(); // 메시지 내용 읽기
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class); // ChatMessage로 변환
        roomId = chatMessage.getRoomId(); // 룸의 UUID 추출
        chatRoom = chatService.findRoomById(chatMessage.getRoomId()); // UUID를 통해 객체 찾기
        chatRoom.handlerAction(session, chatMessage, chatService);
    }

    // 웹소켓 종료 시
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        chatRoom.getSessions().remove(session); // 세션에서 제거
        chatService.deleteRoom(roomId); // 남아있는 사람이 없다면 방 제거
        log.info("WebSocket Close");
    }
}
