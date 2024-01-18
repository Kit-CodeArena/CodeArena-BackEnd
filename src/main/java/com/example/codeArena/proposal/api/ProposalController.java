package com.example.codeArena.proposal.api;


import static com.example.codeArena.exception.CustomException.ErrorCode.INVALID_CONTEXT;

import com.example.codeArena.exception.CustomException;
import com.example.codeArena.proposal.dto.request.CreateProposalRequest;
import com.example.codeArena.proposal.dto.request.UpdateProposalRequest;
import com.example.codeArena.proposal.dto.response.ProposalResponse;
import com.example.codeArena.proposal.service.ProposalService;
import com.example.codeArena.security.UserPrincipal;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test")
public class ProposalController {

    private final ProposalService proposalService;

    /**
     * TODO API LIST
     */

    /**
     * 신청서 작성
     */
    @PostMapping(value = "/room/{roomId}/proposals")
    public ResponseEntity<Void> create
    (
            @PathVariable Long roomId,
            @RequestBody @Valid CreateProposalRequest proposalRequest
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal user)) {
            throw new CustomException(INVALID_CONTEXT);
        }
        proposalService.create(proposalRequest, user.getId(), roomId);
        return ResponseEntity.ok().build();
    }

    /**
     * 신청서 하나 조회
     */
    @GetMapping(value = "/proposals/{proposalId}")
    public ResponseEntity<ProposalResponse> findById
    (
            @PathVariable Long proposalId
    ) {
        ProposalResponse response = proposalService.getById(proposalId);

        return ResponseEntity.ok().body(response);

    }

    /**
     * 사용자가 리더인 모든 방을 조회
     */

    /**
     * 사용자가 리더가 아닌(참여자 인) 모든 방을 조회
     */

    /**
     * 신청서 승인 or 거절
     * TODO : 방을 만들 수 있는 권한 체크 (현재는 ADMIN)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/proposals/{proposalId}")
    public void changeProposalStatus
    (
            @PathVariable Long proposalId,
            @RequestBody @Valid UpdateProposalRequest proposalRequest
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal user)) {
            throw new CustomException(INVALID_CONTEXT);
        }
        proposalService.updateProposalStatus(proposalRequest, user.getId(), proposalId);
    }


    /**
     * 신청서 거절
     */
}
