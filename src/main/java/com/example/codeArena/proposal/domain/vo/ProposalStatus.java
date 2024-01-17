package com.example.codeArena.proposal.domain.vo;

import static com.example.codeArena.exception.CustomException.ErrorCode.INVALID_PROPOSAL_STATUS;

import com.example.codeArena.exception.CustomException;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.Objects;

public enum ProposalStatus {
    WAITING("대기 중"),
    APPROVE("승인"),
    REFUSE("거절"),
    NOT_APPLIED("미신청");

    private final String status;

    ProposalStatus(String status) {
        this.status = status;
    }

    public static ProposalStatus of(String statusName) {
        return Arrays.stream(ProposalStatus.values())
                .filter(status -> Objects.equals(status.getStatus(), statusName))
                .findFirst()
                .orElseThrow(() -> new CustomException(INVALID_PROPOSAL_STATUS));
    }

    @JsonValue
    public String getStatus() {
        return status;
    }
}
