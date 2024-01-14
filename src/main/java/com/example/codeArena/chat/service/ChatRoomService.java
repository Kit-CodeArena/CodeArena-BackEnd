package com.example.codeArena.chat.service;

import static com.example.codeArena.exception.CustomException.ErrorCode.CHAT_ROOM_ALREADY_EXIST;
import static com.example.codeArena.exception.CustomException.ErrorCode.INVALID_INPUT_VALUE_DTO;
import static com.example.codeArena.exception.CustomException.ErrorCode.USER_NOT_FOUND;

import com.example.codeArena.User.model.User;
import com.example.codeArena.User.repository.UserRepository;
import com.example.codeArena.chat.domain.ChatRoom;
import com.example.codeArena.chat.dto.ChatRoomCreateRequest;
import com.example.codeArena.chat.dto.ChatRoomDto;
import com.example.codeArena.chat.repository.ChatRoomRepository;
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

        if (chatRoomRepository.existsByUser(user)) { // 사용자와 관련된 방이 이미 존재하는지 확인
            throw new CustomException(CHAT_ROOM_ALREADY_EXIST);
        }

        ChatRoom chatRoom = ChatRoom.create(dto, user);
        ChatRoom createdChatRoom = chatRoomRepository.save(chatRoom);

        ChatRoomDto response = Optional.of(createdChatRoom)
                .map(ChatRoomDto::new)
                .orElseThrow(()->new CustomException(INVALID_INPUT_VALUE_DTO));

        return response;
    }

    // 모든 채팅방 조회
    @Transactional(readOnly = true)
    public List<ChatRoomDto> findAllRooms() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        return chatRooms.stream()
                .map(ChatRoomDto::new)
                .collect(Collectors.toList());
    }
}
