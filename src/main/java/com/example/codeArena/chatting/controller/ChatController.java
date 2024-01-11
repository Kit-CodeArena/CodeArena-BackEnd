package com.example.codeArena.chatting.controller;

import com.example.codeArena.chatting.domain.ChatRoom;
import com.example.codeArena.chatting.dto.ChatRoomCreateRequest;
import com.example.codeArena.chatting.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test/chat")
public class ChatController {
    private final ChatService chatService;

    // 모든 채팅방 목록
    @GetMapping("/rooms")
    public List<ChatRoom> findAllRoom() {
        return chatService.findAllRoom();
    }

    // 채팅방 생성
    @PostMapping("/room")
    public ChatRoom createRoom(@RequestBody ChatRoomCreateRequest request){
        return chatService.createRoom(request);
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatService.findRoomById(roomId);
    }


}
