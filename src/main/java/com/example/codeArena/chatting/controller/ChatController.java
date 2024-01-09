package com.example.codeArena.chatting.controller;

import com.example.codeArena.chatting.domain.ChatRoom;
import com.example.codeArena.chatting.dto.ChatDto;
import com.example.codeArena.chatting.service.ChatService;
import io.netty.handler.codec.http.HttpContentEncoder.Result;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test/chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping
    public ChatRoom createRoom(@RequestParam String name){
        return chatService.createRoom(name);
    }

    @DeleteMapping
    public void deleteRoom(@RequestParam String name) {
        chatService.deleteRoom(name);
    }

    @GetMapping
    public List<ChatDto> findAllRoom() {
        List<ChatDto> allRoom = chatService.findAllRoom();
        return allRoom;
    }
}
