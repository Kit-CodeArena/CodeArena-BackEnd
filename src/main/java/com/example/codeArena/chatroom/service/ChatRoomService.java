package com.example.codeArena.chatroom.service;

import static com.example.codeArena.exception.CustomException.ErrorCode.CHAT_ROOM_NOT_FOUND;
import static com.example.codeArena.exception.CustomException.ErrorCode.INVALID_INPUT_VALUE_DTO;
import static com.example.codeArena.exception.CustomException.ErrorCode.USER_NOT_FOUND;

import com.example.codeArena.User.domain.User;
import com.example.codeArena.User.repository.UserRepository;
import com.example.codeArena.chatroom.domain.ChatRoom;
import com.example.codeArena.chatroom.domain.vo.ChatRoomStatus;
import com.example.codeArena.chatroom.dto.request.ChatRoomCreateRequest;
import com.example.codeArena.chatroom.dto.response.ChatRoomDetailDto;
import com.example.codeArena.chatroom.dto.response.ChatRoomDto;
import com.example.codeArena.chatroom.dto.response.ChatRoomStatusResponse;
import com.example.codeArena.chatroom.repository.ChatRoomRepository;
import com.example.codeArena.chatroom.strategy.ChatRoomStatusUpdater;
import com.example.codeArena.chatroomuser.domain.ChatRoomUser;
import com.example.codeArena.chatroomuser.domain.vo.ChatRoomUserRole;
import com.example.codeArena.chatroomuser.dto.response.ChatRoomUserResponse;
import com.example.codeArena.chatroomuser.repository.ChatRoomUserRepository;
import com.example.codeArena.exception.CustomException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatRoomStatusUpdater chatRoomStatusUpdater;

    // 채팅방 생성
    @Transactional
    public ChatRoomDto createRoom(ChatRoomCreateRequest dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        ChatRoom chatRoom = ChatRoom.create(dto, user);

        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                .user(user)
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
    public List<ChatRoomDto> findAllRooms(Pageable pageable) {
        Page<ChatRoom> chatRooms = chatRoomRepository.findAll(pageable);
        return chatRooms.stream()
                .map(ChatRoomDto::new)
                .collect(Collectors.toList());
    }

    // 특정 채팅방 조회
    public ChatRoomDetailDto getByUserId(Long userId, Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        List<ChatRoomUserResponse> users = chatRoom.getChatRoomUsers()
                .stream()
                .map(chatRoomUser -> ChatRoomUserResponse.of(chatRoomUser))
                .toList();

        return ChatRoomDetailDto.of(chatRoom, users);
    }

    // 상태 변경
    @Transactional
    public ChatRoomStatusResponse updateStatus(Long roomId, Long userId, ChatRoomStatus status) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND));
        ChatRoomUser chatRoomUser = chatRoomUserRepository.findChatRoomUsersByUserIdAndChatRoomId(userId, chatRoom.getId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        chatRoomUser.isNotLeader(); // leader 인지 체크

        chatRoomStatusUpdater.updateStatus(chatRoom, chatRoomUser, status);

        return new ChatRoomStatusResponse(chatRoom.getStatus());
    }
}
