package com.example.codeArena.proposal.dto.request;

import com.example.codeArena.User.domain.User;
import com.example.codeArena.proposal.domain.Proposal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateProposalRequest {
    @NotNull(message = "해당 채팅방의 리더의 아이디가 비었습니다.")
    Long leaderId;
    @NotBlank(message = "내용을 입력해주세요.")
    String content;

    public Proposal toEntity(CreateProposalRequest dto, User user, Long roomId) {
        return Proposal.builder()
                .user(user)
                .leaderId(dto.getLeaderId())
                .roomId(roomId)
                .content(dto.getContent())
                .build();
    }
}
