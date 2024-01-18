package com.example.codeArena.chat.handler;

import static com.example.codeArena.exception.CustomException.ErrorCode.AUTH_HEADER_NOT_FOUND;
import static com.example.codeArena.exception.CustomException.ErrorCode.BLOCK_USER_FROM_CHAT_ROOM;
import static com.example.codeArena.exception.CustomException.ErrorCode.USER_NOT_FOUND;
import static com.example.codeArena.exception.CustomException.ErrorCode.VALUE_NOT_FOUNE;
import static com.example.codeArena.exception.CustomException.ErrorCode.WEB_SOCKET_SA_NULL;

import com.example.codeArena.User.domain.User;
import com.example.codeArena.User.repository.UserRepository;
import com.example.codeArena.User.util.JwtTokenProvider;
import com.example.codeArena.chatroomuser.domain.ChatRoomUser;
import com.example.codeArena.chatroomuser.domain.vo.ChatRoomUserRole;
import com.example.codeArena.chatroomuser.repository.ChatRoomUserRepository;
import com.example.codeArena.exception.CustomException;

import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    public static final String DEFAULT_PATH = "/topic/public/";
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    // websocket을 통해 들어온 요청이 처리 되기전 실행.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        if (StompCommand.CONNECT.equals(command)) { // websocket 연결요청 -> JWT 인증
            // JWT 인증
            User user = getUserByAuthorizationHeader(accessor.getFirstNativeHeader("Authorization"));

            // 인증 후 데이터를 헤더에 추가
            setValue(accessor, "userId", user.getId());
            setValue(accessor, "username", user.getUsername());
            setValue(accessor, "userNickname", user.getNickname());
            // 이메일 - 필요하다면

        } else if (StompCommand.SUBSCRIBE.equals(command)) { // 채팅룸 구독요청(진입) -> ChatRoomUser인지 검증

            Long userId = (Long)getValue(accessor, "userId");
            Long roomId = parseChatRoomIdFromPath(accessor);
            setValue(accessor, "roomId", roomId);

            // 방에 존재하는 User 인지 검사
            validateUserInChatRoom(userId, roomId);

        } else if (StompCommand.DISCONNECT == command) { // Websocket 연결 종료
            Long userId = (Long)getValue(accessor, "userId");
            log.info("DISCONNECTED userId : {}", userId);
        }

        log.info("header : " + message.getHeaders());
        log.info("message:" + message);

        return message;
    }

    private User getUserByAuthorizationHeader(String authHeaderValue) {
        // accessToken 받아오기
        String accessToken = getTokenByAuthorizationHeader(authHeaderValue);

        /**
         * jwt claim 에 nickname 을 넣어놔서 nickname으로 찾아야 함.
         * 추후 변경..? 참고
         */
        String userNickname = jwtTokenProvider.getNicknameFromJWT(accessToken);

        return userRepository.findByNickname(userNickname).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    private String getTokenByAuthorizationHeader(String authHeaderValue) {
        if (Objects.isNull(authHeaderValue) || authHeaderValue.isBlank()) {
            throw new CustomException(AUTH_HEADER_NOT_FOUND);
        }

        String accessToken = extractToken(authHeaderValue);
        jwtTokenProvider.validateToken(accessToken); // jwt 검증 예외 처리

        return accessToken;
    }

    public String extractToken(String authHeaderValue) {
        if (authHeaderValue.toLowerCase().startsWith("Bearer".toLowerCase())) {
            return authHeaderValue.substring("Bearer".length()).trim();
        }
        return null;
    }

    private void setValue(StompHeaderAccessor accessor, String key, Object value) {
        Map<String, Object> sessionAttributes = getSessionAttributes(accessor);
        sessionAttributes.put(key, value);
    }

    private Map<String, Object> getSessionAttributes(StompHeaderAccessor accessor) {
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

        if (Objects.isNull(sessionAttributes)) {
            throw new CustomException(WEB_SOCKET_SA_NULL);
        }
        return sessionAttributes;
    }

    private Object getValue(StompHeaderAccessor accessor, String key) {
        Map<String, Object> sessionAttributes = getSessionAttributes(accessor);
        Object value = sessionAttributes.get(key);

        if (Objects.isNull(value)) {
            throw new CustomException(VALUE_NOT_FOUNE);
        }

        return value;
    }

    private Long parseChatRoomIdFromPath(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();
        return Long.parseLong(destination.substring(DEFAULT_PATH.length()));
    }

    private void validateUserInChatRoom(Long userId, Long roomId) {
        ChatRoomUser chatRoomUser = chatRoomUserRepository.findChatRoomUsersByUserIdAndChatRoomId(userId, roomId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if(chatRoomUser.getChatRoomUserRole().equals(ChatRoomUserRole.BLOCKED)){
            throw new CustomException(BLOCK_USER_FROM_CHAT_ROOM);
        }
    }

}
