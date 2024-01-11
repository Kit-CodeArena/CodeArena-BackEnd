package com.example.codeArena.chatting.dto;

import com.example.codeArena.chatting.domain.Tag;
import lombok.Getter;

@Getter
public class ChatRoomCreateRequest {
    private String name;
    private int maxUserNum;
    private Tag tag;
    // TODO: User 추가
}
