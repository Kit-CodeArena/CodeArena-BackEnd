package com.example.codeArena.chat.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    public enum MessageType {
        ENTER, TALK, ERROR
    }

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
}
