package com.example.codeArena.chat.handler;

import com.example.codeArena.chat.dto.request.ChatRequest;
import com.example.codeArena.chat.dto.request.MessageType;
import com.example.codeArena.exception.CustomException;
import com.example.codeArena.exception.CustomException.ErrorCode;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    // 연결 요청
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Received a new web socket connection");
    }

    // 구독 요청(입장)
    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
        log.info("Received a new web socket subscribe");
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String)getValue(accessor, "username");
        Long userId = (Long)getValue(accessor, "userId");
        Long roomId = (Long)getValue(accessor, "roomId");

        log.info("User: {} {} Subscribe Crew : {}", userId, username, roomId);
        ChatRequest chatRequest = new ChatRequest(MessageType.JOIN, userId,
                username + " 님이 입장했습니다.");
        messagingTemplate.convertAndSend("/topic/public/" + roomId, chatRequest);

    }

    // 연결 해제
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String)getValue(accessor, "username");
        Long userId = (Long)getValue(accessor, "userId");
        Long roomId = (Long)getValue(accessor, "roomId");

        log.info("User: {} {} Disconnected Crew : {}", userId, username, roomId);

        ChatRequest chatRequest = new ChatRequest(
                MessageType.LEAVE, userId, username + " 님이 떠났습니다.");

        messagingTemplate.convertAndSend("/topic/public/" + roomId, chatRequest);
    }

    private Object getValue(StompHeaderAccessor accessor, String key) {
        Map<String, Object> sessionAttributes = getSessionAttributes(accessor);
        Object value = sessionAttributes.get(key);

        if (Objects.isNull(value)) {
            throw new CustomException(ErrorCode.VALUE_NOT_FOUNE);
        }

        return value;
    }

    private Map<String, Object> getSessionAttributes(StompHeaderAccessor accessor) {
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

        if (Objects.isNull(sessionAttributes)) {
            throw new CustomException(ErrorCode.WEB_SOCKET_SA_NULL);
        }
        return sessionAttributes;
    }
}
