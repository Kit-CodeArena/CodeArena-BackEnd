package com.example.codeArena.proposal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class ProposalPageResponse {
    Page<ProposalResponse> responses;
}
