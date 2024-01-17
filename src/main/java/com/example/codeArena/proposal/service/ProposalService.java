package com.example.codeArena.proposal.service;

import com.example.codeArena.User.model.User;
import com.example.codeArena.User.repository.UserRepository;
import com.example.codeArena.chatroom.domain.ChatRoom;
import com.example.codeArena.chatroom.domain.vo.ChatRoomStatus;
import com.example.codeArena.chatroom.repository.ChatRoomRepository;
import com.example.codeArena.chatroomuser.domain.ChatRoomUser;
import com.example.codeArena.chatroomuser.repository.ChatRoomUserRepository;
import com.example.codeArena.exception.CustomException;
import com.example.codeArena.exception.CustomException.ErrorCode;
import com.example.codeArena.proposal.domain.Proposal;
import com.example.codeArena.proposal.domain.vo.ProposalStatus;
import com.example.codeArena.proposal.dto.request.CreateProposalRequest;
import com.example.codeArena.proposal.repository.ProposalRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProposalService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ProposalRepository proposalRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    public void create(CreateProposalRequest proposalRequest, Long userId, Long roomId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        ChatRoom room = chatRoomRepository.findById(roomId)
                .filter(c -> c.getStatus().equals(ChatRoomStatus.OPEN))
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        proposalRepository.findProposalsByUserIdAndChatRoomId(userId, roomId)
                .filter(p -> p.getStatus().equals(ProposalStatus.WAITING))
                .ifPresent(proposal -> { throw new CustomException(ErrorCode.PROPOSAL_ALREADY_EXIST); });

        Integer currentChatRoomUserCount = chatRoomUserRepository.countChatRoomUsersByChatRoomId(roomId);

        if (currentChatRoomUserCount >= room.getMaxUserNum()) {
            throw new CustomException(ErrorCode.CHAT_ROOM_USER_COUNT_OVER_CAPACITY);
        }

        Optional<ChatRoomUser> chatRoomUser = chatRoomUserRepository.findChatRoomUsersByUserIdAndChatRoomId(userId, roomId);

        if (chatRoomUser.isPresent()) {
            switch (chatRoomUser.get().getChatRoomUserRole()) {
                case BLOCKED -> throw new CustomException(ErrorCode.DUPLICATE_BLOCKED_USER_FROM_CHAT_RROM);
                case LEADER -> throw new CustomException(ErrorCode.DUPLICATE_LEADER_USER_FROM_CHAT_RROM);
                case USER -> throw new CustomException(ErrorCode.DUPLICATE_USER_FROM_CHAT_ROOM);
            }
        }

        Proposal proposal = proposalRequest.toEntity(proposalRequest, user, roomId);
        proposalRepository.save(proposal);
    }
}
