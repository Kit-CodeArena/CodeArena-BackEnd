package com.example.codeArena.chat.dto.response;

import com.example.codeArena.User.domain.User;
import com.example.codeArena.chat.domain.Chat;
import com.example.codeArena.chat.dto.request.MessageType;
import com.example.codeArena.proposal.dto.response.UserProposalResponse;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatResponse {
    private String id;
    private UserProposalResponse user;
    private MessageType type;
    private LocalDateTime createdAt;
    private String content;

    public ChatResponse(Chat chat, String userNickname) {
        this.id = chat.getId();
        this.user = new UserProposalResponse(chat.getUserId(), userNickname);
        this.type = chat.getMessageType();
        this.createdAt = LocalDateTime.now();
        this.content = chat.getContent();
    }
}
