package com.example.codeArena.chat.controller;

import com.example.codeArena.chat.dto.request.ChatRequest;
import com.example.codeArena.chat.dto.response.ChatResponse;
import com.example.codeArena.chat.service.ChatService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

    private final ChatService chatService;

    /**
     * 채팅 저장
     */
    @MessageMapping("/chat.sendMessage/{roomId}")
    @SendTo("/topic/public/{roomId}")
    public ChatResponse sendMessage(
            @DestinationVariable Long roomId,
            @Header("simpSessionAttributes") Map<Object, Object> simpSessionAttributes,
            @Payload ChatRequest chatRequest
    ) {
        return chatService.save(chatRequest, roomId, simpSessionAttributes);
    }

    /**
     * 채팅 목록
     */
    @GetMapping("/api/room/{roomId}/chats")
    public ResponseEntity<List<ChatResponse>> getChattingList(
            @PathVariable(name = "roomId") Long roomId,
            @PageableDefault(sort = "createdAt") Pageable pageable) {

        List<ChatResponse> response = chatService.getByRoomId(roomId, pageable);

        return ResponseEntity.ok(response);
    }

}
