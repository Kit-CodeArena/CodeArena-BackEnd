package com.example.codeArena.chatroomuser.api;

import static com.example.codeArena.exception.CustomException.ErrorCode.INVALID_CONTEXT;

import com.example.codeArena.chatroomuser.dto.request.UpdateChatRoomUserRequest;
import com.example.codeArena.chatroomuser.service.ChatRoomUserService;
import com.example.codeArena.exception.CustomException;
import com.example.codeArena.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test/room")
public class ChatRoomUserController {

    private final ChatRoomUserService chatRoomUserService;

    /**
     * 채팅방 사용자 강퇴 가능
     */
    @PatchMapping(value = "/{roomId}/user")
    public ResponseEntity<Void> block(
            @RequestBody @Valid UpdateChatRoomUserRequest dto,
            @PathVariable Long roomId
            ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal user)) {
            throw new CustomException(INVALID_CONTEXT);
        }
        chatRoomUserService.block(user.getId(), dto.getBlockUserId(), roomId);
        return ResponseEntity.ok().build();
    }

    /**
     * 방 나가기 기능.
     * 방을 나가면 해당 채팅방 인원에서 삭제
     */

}
