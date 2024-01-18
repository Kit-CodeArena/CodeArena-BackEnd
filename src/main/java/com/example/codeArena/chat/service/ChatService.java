package com.example.codeArena.chat.service;

import static com.example.codeArena.exception.CustomException.ErrorCode.INVALID_INPUT_VALUE_DTO;

import com.example.codeArena.chat.domain.Chat;
import com.example.codeArena.chat.dto.request.ChatRequest;
import com.example.codeArena.chat.dto.response.ChatResponse;
import com.example.codeArena.chat.repository.ChatRepository;
import com.example.codeArena.exception.CustomException;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    @Transactional
    public ChatResponse save(ChatRequest chatRequest, Long roomId, Map<String, Object> header) {
        Chat chat = Chat.builder()
                .roomId(roomId)
                .userId(chatRequest.getUserId())
                .messageType(chatRequest.getType())
                .content(chatRequest.getContent())
                .build();
        Chat savedChat = chatRepository.save(chat);
        return toChatResponse(savedChat, header);
    }

    private ChatResponse toChatResponse(Chat chat, Map<String, Object> header) {
        String userNickname = getValueFromHeader(header, "userNickname");
        return Optional.of(chat)
                .map(c -> new ChatResponse(c, userNickname))
                .orElseThrow(()->new CustomException(INVALID_INPUT_VALUE_DTO));
    }

    private String getValueFromHeader(Map<String, Object> header, String key) {
        return (String)header.get(key);
    }
}
