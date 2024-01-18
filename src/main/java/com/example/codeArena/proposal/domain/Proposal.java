package com.example.codeArena.proposal.domain;

import static com.example.codeArena.proposal.domain.vo.ProposalStatus.APPROVE;
import static jakarta.persistence.FetchType.LAZY;

import com.example.codeArena.User.model.User;
import com.example.codeArena.proposal.domain.vo.ProposalStatus;
import com.example.codeArena.proposal.dto.request.CreateProposalRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "proposals")
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long leaderId;

    private Long chatRoomId;

    @Column(nullable = false, length = 100)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProposalStatus status;

    @Builder
    public Proposal(User user, Long leaderId, Long roomId, String content, ProposalStatus status) {
        this.user = user;
        this.leaderId = leaderId;
        this.chatRoomId = roomId;
        this.content = content;
        this.status = ProposalStatus.WAITING;
    }

    public boolean isApprove(ProposalStatus statusName) {
        return Objects.equals(statusName, APPROVE);
    }

    public void changeProposalStatus(ProposalStatus status) {
        this.status = status;
    }
}
