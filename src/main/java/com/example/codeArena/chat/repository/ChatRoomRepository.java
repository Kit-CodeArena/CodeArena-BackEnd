package com.example.codeArena.chat.repository;

import com.example.codeArena.User.model.User;
import com.example.codeArena.chat.domain.ChatRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    boolean existsByUser(User user);

    Optional<ChatRoom> findByRoomId(String roomId);
}
