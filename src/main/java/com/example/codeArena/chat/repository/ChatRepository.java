package com.example.codeArena.chat.repository;

import com.example.codeArena.chat.domain.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Chat, Long>{
}
