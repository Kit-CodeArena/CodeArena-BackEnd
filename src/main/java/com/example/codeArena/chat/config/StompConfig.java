package com.example.codeArena.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class StompConfig implements WebSocketMessageBrokerConfigurer, ChannelInterceptor {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat").setAllowedOriginPatterns("*");
    }

    // 메시지 브로커 설정을 위한 메서드
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        /*
         * 스프링에서 제공하는 내장 브로커를 사용
         * queue 라는 prefix 는 메시지가 1:1 송신 시, topic 이라는 prefix 는 메시지가 1:N 여러명에게 송신 시
         */
        registry.enableSimpleBroker("/queue","/topic");

        // 메시지의 처리나 가공이 필요한 경우 핸들러를 타게 함.
        registry.setApplicationDestinationPrefixes("/app");
    }
}
