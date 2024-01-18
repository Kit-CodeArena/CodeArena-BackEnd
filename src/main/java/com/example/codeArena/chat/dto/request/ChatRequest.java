package com.example.codeArena.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRequest {
    MessageType type;
    Long userId;
    String content;
}
