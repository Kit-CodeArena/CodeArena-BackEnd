package com.example.codeArena.chatroom.api;

import static com.example.codeArena.exception.CustomException.ErrorCode.INVALID_CONTEXT;

import com.example.codeArena.chatroom.dto.request.ChatRoomCreateRequest;
import com.example.codeArena.chatroom.dto.request.UpdateChatRoomStatus;
import com.example.codeArena.chatroom.dto.response.ChatRoomDetailDto;
import com.example.codeArena.chatroom.dto.response.ChatRoomDto;
import com.example.codeArena.chatroom.dto.response.ChatRoomStatusResponse;
import com.example.codeArena.chatroom.service.ChatRoomService;
import com.example.codeArena.exception.CustomException;
import com.example.codeArena.security.UserPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    /**
     * TODO : ADMIN 이 아닌 채팅방을 생성할 수 있는 다른 권한으로 변환 필요
     * 채팅방 생성
     * 현재는 ADMIN 권한을 가진 사람만 생성 가능
     * TODO 2 - 대회용 문제 제출 여/부 확인 추가
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/room")
    public ChatRoomDto createRoom(@RequestBody @Valid ChatRoomCreateRequest request){
        // 토큰 확인 & 정보 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal user)) {
            throw new CustomException(INVALID_CONTEXT);
        }
        return chatRoomService.createRoom(request, user.getId());
    }

    // 모든 채팅방 목록
    @GetMapping(value = "/rooms")
    public List<ChatRoomDto> findAllRoom(
            @PageableDefault Pageable pageable
    ) {
        return chatRoomService.findAllRooms(pageable);
    }

    /**
     * TODO : 검색 API 추후 추가 예정
     * Tag, nickname, name 을 통해 채팅방을 검색하게 함.
     */

    /**
     * 특정 채팅방 조회
     */
    @GetMapping(value = "/room/{roomId}")
    public ResponseEntity<?> getById(@PathVariable Long roomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal user)) {
            throw new CustomException(INVALID_CONTEXT);
        }
        ChatRoomDetailDto response = chatRoomService.getByUserId(user.getId(), roomId);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 상태 변경
     * 채팅방을 생성할 수 있는 ADMIN만 채팅방의 상태 변경 가능
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/room/{roomId}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long roomId,
            @RequestBody @Valid UpdateChatRoomStatus dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal user)) {
            throw new CustomException(INVALID_CONTEXT);
        }
        ChatRoomStatusResponse response = chatRoomService.updateStatus(roomId, user.getId(), dto.getStatus());
        return ResponseEntity.ok().body(response);
    }
}
