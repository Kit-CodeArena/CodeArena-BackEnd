package com.example.codeArena.chatroomuser.repository;

import com.example.codeArena.chatroomuser.domain.ChatRoomUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {
    Optional<ChatRoomUser> findChatRoomUsersByUserIdAndChatRoomId(Long userId, Long chatRoomId);

    Integer countChatRoomUsersByChatRoomId(Long roomId);
}
