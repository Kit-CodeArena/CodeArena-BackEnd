package com.example.codeArena.chatting.repository;

import com.example.codeArena.chatting.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
