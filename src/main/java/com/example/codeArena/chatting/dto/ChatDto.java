package com.example.codeArena.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatDto {
    String roomId;
    String name;
    long size;
}
