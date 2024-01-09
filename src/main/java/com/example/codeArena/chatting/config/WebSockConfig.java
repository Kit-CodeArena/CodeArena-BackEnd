package com.example.codeArena.chatting.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket // 웹 소켓 활성화
public class WebSockConfig implements WebSocketConfigurer {
    private final WebSocketChatHandler handler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        /*
         * 클라이언트가 보낼 통신을 처리할 핸들러
         * 직접 구현한 웹소켓 핸들러를 웹소켓이 연결될 때, handshake할 주소와 함께 addHandler 메서의 인자로 넣음
         */
        registry.addHandler(handler,"/ws/chat")
                .setAllowedOrigins("*"); // cors 설정
    }
}
