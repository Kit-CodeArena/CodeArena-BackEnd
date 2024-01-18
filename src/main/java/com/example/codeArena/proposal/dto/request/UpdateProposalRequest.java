package com.example.codeArena.proposal.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateProposalRequest {
    @NotBlank(message = "변환하려는 상태가 존재하지 않습니다.")
    String proposalStatus;
}
