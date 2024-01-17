package com.example.codeArena.proposal.domain;

import static jakarta.persistence.FetchType.LAZY;

import com.example.codeArena.User.model.User;
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
import lombok.Getter;

@Getter
@Entity
@Table(name = "proposals")
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long leaderId;

    private Long roomId;

    @Column(nullable = false, length = 100)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProposalStatus status;

}
