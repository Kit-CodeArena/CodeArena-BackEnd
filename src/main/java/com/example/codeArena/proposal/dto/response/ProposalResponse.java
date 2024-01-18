package com.example.codeArena.proposal.dto.response;

import com.example.codeArena.chatroom.domain.ChatRoom;
import com.example.codeArena.chatroom.dto.response.ChatRoomDto;
import com.example.codeArena.proposal.domain.Proposal;
import com.example.codeArena.proposal.domain.vo.ProposalStatus;
import lombok.Getter;

@Getter
public class ProposalResponse {
    private Long id;
    private UserProposalResponse user;
    private ChatRoomDto room;
    private String content;
    private ProposalStatus status;

    public ProposalResponse(Proposal proposal, ChatRoom chatRoom) {
        this.id = proposal.getId();
        this.user = new UserProposalResponse(proposal.getUser().getId(), proposal.getUser().getNickname());
        this.room = new ChatRoomDto(chatRoom);
        this.content = proposal.getContent();
        this.status = proposal.getStatus();
    }
}
