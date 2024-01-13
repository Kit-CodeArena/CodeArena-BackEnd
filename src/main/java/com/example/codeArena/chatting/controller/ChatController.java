package com.example.codeArena.chatting.controller;

import static com.example.codeArena.exception.CustomException.ErrorCode.INVALID_CONTEXT;

import com.example.codeArena.chatting.domain.ChatRoom;
import com.example.codeArena.chatting.dto.ChatRoomCreateRequest;
import com.example.codeArena.chatting.dto.ChatRoomDto;
import com.example.codeArena.chatting.service.ChatService;
import com.example.codeArena.exception.CustomException;
import com.example.codeArena.security.UserPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test/chat")
public class ChatController {
    private final ChatService chatService;

    // 모든 채팅방 목록
    @GetMapping("/rooms")
    public List<ChatRoomDto> findAllRoom() {
        return chatService.findAllRooms();
    }

    /*
     * 채팅방 생성
     * 현재는 ADMIN 권한을 가진 사람만 생성 가능
     * TODO : ADMIN 이 아닌 채팅방을 생성할 수 있는 다른 권한으로 변환 필요
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/room")
    public ChatRoomDto createRoom(@RequestBody @Valid ChatRoomCreateRequest request){
        // 토큰 확인 & 정보 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal user)) {
            throw new CustomException(INVALID_CONTEXT);
        }
        return chatService.createRoom(request, user.getId());
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatService.findRoomById(roomId);
    }


}
