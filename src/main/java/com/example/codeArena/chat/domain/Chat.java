package com.example.codeArena.chat.domain;

import static org.springframework.data.mongodb.core.index.IndexDirection.ASCENDING;

import com.example.codeArena.chat.dto.request.MessageType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@Document(collection = "chat")
public class Chat {
    @Id
    private String id;

    @Indexed(direction = ASCENDING)  // 인덱스 추가 (오름 차순)
    private Long roomId;

    private Long userId;

    private MessageType messageType;

    private String content;

    @Builder
    public Chat(Long roomId, Long userId, MessageType messageType, String content) {
        this.roomId = roomId;
        this.userId = userId;
        this.messageType = messageType;
        this.content = content;
    }
}
