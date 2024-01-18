package com.example.codeArena.chat.service;

import static com.example.codeArena.exception.CustomException.ErrorCode.INVALID_INPUT_VALUE_DTO;

import com.example.codeArena.chat.domain.Chat;
import com.example.codeArena.chat.dto.request.ChatRequest;
import com.example.codeArena.chat.dto.response.ChatResponse;
import com.example.codeArena.chat.repository.ChatRepository;
import com.example.codeArena.chatroom.domain.ChatRoom;
import com.example.codeArena.exception.CustomException;
import com.example.codeArena.exception.CustomException.ErrorCode;
import com.example.codeArena.proposal.dto.response.ProposalResponse;
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
    public ChatResponse save(ChatRequest chatRequest, Long roomId, Map<String, Object> header) {
        String userNickname = getValueFromHeader(header, "userNickname");
        Chat chat = Chat.builder()
                .roomId(roomId)
                .userId(chatRequest.getUserId())
                .userNickname(userNickname)
                .messageType(chatRequest.getType())
                .content(chatRequest.getContent())
                .build();
        Chat savedChat = chatRepository.save(chat);
        return toChatResponse(savedChat, userNickname);
    }

    private String getValueFromHeader(Map<String, Object> header, String key) {
        return (String)header.get(key);
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
