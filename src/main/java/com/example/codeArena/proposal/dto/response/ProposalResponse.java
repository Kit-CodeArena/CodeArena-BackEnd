package com.example.codeArena.proposal.dto.response;

import com.example.codeArena.chatroom.domain.ChatRoom;
import com.example.codeArena.chatroom.dto.response.ChatRoomDto;
import com.example.codeArena.proposal.domain.Proposal;
import com.example.codeArena.proposal.domain.vo.ProposalStatus;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProposalResponse {
    private Long id;
    private UserProposalResponse user;
    private ChatRoomDto room;
    private String content;
    private ProposalStatus status;
    private LocalDateTime createdAt;

    public ProposalResponse(Proposal proposal, ChatRoom chatRoom) {
        this.id = proposal.getId();
        this.user = new UserProposalResponse(proposal.getUser().getId(), proposal.getUser().getNickname());
        this.room = new ChatRoomDto(chatRoom);
        this.content = proposal.getContent();
        this.status = proposal.getStatus();
        this.createdAt = proposal.getCreatedAt();
    }
}
