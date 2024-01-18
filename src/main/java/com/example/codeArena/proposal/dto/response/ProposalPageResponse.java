package com.example.codeArena.proposal.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProposalPageResponse {
    List<ProposalResponse> responses;
}
