package com.example.codeArena.proposal.repository;

import com.example.codeArena.proposal.domain.Proposal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    Optional<Proposal> findProposalsByUserIdAndChatRoomId(Long userId, Long roomId);
}
