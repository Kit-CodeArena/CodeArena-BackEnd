package com.example.codeArena.chatting.domain;

import com.example.codeArena.chatting.dto.ChatMessage;
import com.example.codeArena.chatting.dto.ChatMessage.MessageType;
import com.example.codeArena.chatting.service.ChatService;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

@Getter
public class ChatRoom {
    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public void handlerAction(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {
        if(chatMessage.getType().equals(MessageType.ENTER)) { // 입장 시
            sessions.add(session); // 세션에 담기
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");
            sendMessage(chatMessage, chatService);
        } else if (chatMessage.getType().equals(MessageType.TALK)) { // 대화 시
            chatMessage.setMessage(chatMessage.getMessage());
            sendMessage(chatMessage, chatService);
        }
    }

    private <T> void sendMessage(T message, ChatService chatService) {
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session,message));
    }
}
