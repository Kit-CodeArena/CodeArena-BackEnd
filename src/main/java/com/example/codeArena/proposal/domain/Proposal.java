package com.example.codeArena.proposal.domain;

import static com.example.codeArena.proposal.domain.vo.ProposalStatus.APPROVE;
import static jakarta.persistence.FetchType.LAZY;

import com.example.codeArena.User.domain.User;
import com.example.codeArena.proposal.domain.vo.ProposalStatus;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

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

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Proposal(User user, Long leaderId, Long roomId, String content, ProposalStatus status, LocalDateTime createdAt) {
        this.user = user;
        this.leaderId = leaderId;
        this.chatRoomId = roomId;
        this.content = content;
        this.status = ProposalStatus.WAITING;
        this.createdAt = LocalDateTime.now();
    }

    public boolean isApprove(ProposalStatus statusName) {
        return Objects.equals(statusName, APPROVE);
    }

    public void changeProposalStatus(ProposalStatus status) {
        this.status = status;
    }
}
