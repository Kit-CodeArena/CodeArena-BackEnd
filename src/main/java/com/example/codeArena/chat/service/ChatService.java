package com.example.codeArena.chat.service;

import static com.example.codeArena.exception.CustomException.ErrorCode.INVALID_INPUT_VALUE_DTO;

import com.example.codeArena.chat.domain.Chat;
import com.example.codeArena.chat.dto.request.ChatRequest;
import com.example.codeArena.chat.dto.response.ChatResponse;
import com.example.codeArena.chat.repository.ChatRepository;
import com.example.codeArena.exception.CustomException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    @Transactional
    public ChatResponse save(ChatRequest chatRequest, Long roomId, Map<Object, Object> header) {
        String userNickname = (String) getValueFromHeader(header, "userNickname");
        Long userId = (Long) getValueFromHeader(header, "userId");
        Chat chat = Chat.builder()
                .roomId(roomId)
                .userId(userId)
                .userNickname(userNickname)
                .messageType(chatRequest.getType())
                .content(chatRequest.getContent())
                .build();
        Chat savedChat = chatRepository.save(chat);
        return toChatResponse(savedChat, userNickname);
    }

    private Object getValueFromHeader(Map<Object, Object> header, Object key) {
        return header.get(key);
    }

    private ChatResponse toChatResponse(Chat chat, String nickname) {
        return Optional.of(chat)
                .map(c -> new ChatResponse(c, nickname))
                .orElseThrow(()->new CustomException(INVALID_INPUT_VALUE_DTO));
    }



    public List<ChatResponse> getByRoomId(Long roomId, Pageable pageable) {
        List<ChatResponse> responses = chatRepository.findByRoomId(roomId, pageable)
                .map(chat -> {
                    return Optional.of(chat)
                            .map(c -> new ChatResponse(c, c.getUserNickname()))
                            .orElseThrow(()->new CustomException(INVALID_INPUT_VALUE_DTO));
                })
                .toList();
        return responses;
    }
}
