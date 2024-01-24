package com.example.codeArena.chat.dto.request;

import com.example.codeArena.chat.domain.Chat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRequest {
    MessageType type;
    Long userId;
    String content;
}
