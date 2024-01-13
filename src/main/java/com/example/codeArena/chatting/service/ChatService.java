package com.example.codeArena.chatting.service;

import static com.example.codeArena.exception.CustomException.ErrorCode.INVALID_INPUT_VALUE;
import static com.example.codeArena.exception.CustomException.ErrorCode.INVALID_INPUT_VALUE_DTO;
import static com.example.codeArena.exception.CustomException.ErrorCode.USER_NOT_FOUND;

import com.example.codeArena.User.model.User;
import com.example.codeArena.User.repository.UserRepository;
import com.example.codeArena.chatting.domain.ChatRoom;
import com.example.codeArena.chatting.dto.ChatRoomCreateRequest;
import com.example.codeArena.chatting.dto.ChatRoomDto;
import com.example.codeArena.chatting.repository.ChatRoomRepository;
import com.example.codeArena.exception.CustomException;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private Map<String, ChatRoom> chatRooms;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 의존 관계 주입 완료 후 실행
    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    // 모든 채팅방 조회
    public List<ChatRoom> findAllRoom() {
        List<ChatRoom> result = new ArrayList<>(chatRooms.values());
        Collections.reverse(result); // 최근 생성 순
        return result;
    }

    // 채팅방 하나 조회
    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    // 새로운 방 생성
    // TODO: User 연관 관계 설정 후, 입력으로 받아서 방을 만들 때 방 만든 사람 정보 추가
    public ChatRoomDto createRoom(ChatRoomCreateRequest dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        ChatRoom chatRoom = ChatRoom.create(dto, user);
        ChatRoom createdChatRoom = chatRoomRepository.save(chatRoom);

        ChatRoomDto response = Optional.of(createdChatRoom)
                .map(ChatRoomDto::new)
                .orElseThrow(()->new CustomException(INVALID_INPUT_VALUE_DTO));

        return response;
    }
}
