package com.example.codeArena.chat.repository;

import com.example.codeArena.chat.domain.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Chat, Long>{
    Page<Chat> findByRoomId(Long roomId, Pageable pageable);
}
