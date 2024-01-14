package com.example.codeArena.chat.controller;

import static com.example.codeArena.exception.CustomException.ErrorCode.INVALID_CONTEXT;

import com.example.codeArena.chat.dto.ChatRoomCreateRequest;
import com.example.codeArena.chat.dto.ChatRoomDto;
import com.example.codeArena.chat.service.ChatRoomService;
import com.example.codeArena.exception.CustomException;
import com.example.codeArena.security.UserPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test/chat")
public class ChatController {
    private final ChatRoomService chatService;

    // 모든 채팅방 목록
    @GetMapping("/rooms")
    public List<ChatRoomDto> findAllRoom() {
        return chatService.findAllRooms();
    }

    /**
     * TODO : ADMIN 이 아닌 채팅방을 생성할 수 있는 다른 권한으로 변환 필요
     * 채팅방 생성
     * 현재는 ADMIN 권한을 가진 사람만 생성 가능
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

    /**
     * TODO : 검색 API 추후 추가 예정
     * Tag, nickname, name 을 통해 채팅방을 검색하게 함.
     */

}