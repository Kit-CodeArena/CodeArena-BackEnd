package com.example.codeArena.chatroom.service;

import static com.example.codeArena.exception.CustomException.ErrorCode.INVALID_INPUT_VALUE_DTO;
import static com.example.codeArena.exception.CustomException.ErrorCode.USER_NOT_FOUND;

import com.example.codeArena.User.model.User;
import com.example.codeArena.User.repository.UserRepository;
import com.example.codeArena.chatroom.domain.ChatRoom;
import com.example.codeArena.chatroom.dto.request.ChatRoomCreateRequest;
import com.example.codeArena.chatroom.dto.response.ChatRoomDto;
import com.example.codeArena.chatroom.repository.ChatRoomRepository;
import com.example.codeArena.chatroomuser.domain.ChatRoomUser;
import com.example.codeArena.chatroomuser.domain.vo.ChatRoomUserRole;
import com.example.codeArena.exception.CustomException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoomDto createRoom(ChatRoomCreateRequest dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        ChatRoom chatRoom = ChatRoom.create(dto);

        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                .userId(user.getId())
                .chatRoom(chatRoom)
                .chatRoomUserRole(ChatRoomUserRole.LEADER)
                .build();
        chatRoom.addRoomMember(chatRoomUser);

        ChatRoom createdChatRoom = chatRoomRepository.save(chatRoom);
        ChatRoomDto response = Optional.of(createdChatRoom)
                .map(ChatRoomDto::new)
                .orElseThrow(()->new CustomException(INVALID_INPUT_VALUE_DTO));

        return response;
    }

    // 모든 채팅방 조회
    public List<ChatRoomDto> findAllRooms() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        return chatRooms.stream()
                .map(ChatRoomDto::new)
                .collect(Collectors.toList());
    }
}
